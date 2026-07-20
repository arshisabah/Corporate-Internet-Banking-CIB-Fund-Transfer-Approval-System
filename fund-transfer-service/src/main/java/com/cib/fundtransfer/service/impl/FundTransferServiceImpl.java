package com.cib.fundtransfer.service.impl;

import com.cib.common.dto.ApiResponse;
import com.cib.common.exception.BeneficiaryNotActiveException;
import com.cib.common.exception.DownstreamServiceException;
import com.cib.common.exception.InvalidStateTransitionException;
import com.cib.common.exception.ResourceNotFoundException;
import com.cib.fundtransfer.client.BeneficiaryClient;
import com.cib.fundtransfer.client.ApprovalClient;
import com.cib.fundtransfer.client.dto.BeneficiaryValidationResponse;
import com.cib.fundtransfer.client.dto.CreateApprovalRequestDto;
import com.cib.fundtransfer.dto.request.InitiateTransferRequest;
import com.cib.fundtransfer.dto.request.ResubmitTransferRequest;
import com.cib.fundtransfer.dto.request.UpdateTransferStatusRequest;
import com.cib.fundtransfer.dto.response.TransferResponse;
import com.cib.fundtransfer.dto.response.TransferStatusHistoryResponse;
import com.cib.fundtransfer.entity.FundTransfer;
import com.cib.fundtransfer.entity.TransferStatusHistory;
import com.cib.fundtransfer.enums.TransferStatus;
import com.cib.fundtransfer.mapper.TransferMapper;
import com.cib.fundtransfer.repository.FundTransferRepository;
import com.cib.fundtransfer.repository.TransferStatusHistoryRepository;
import com.cib.fundtransfer.service.FundTransferService;
import com.cib.fundtransfer.validator.TransferStatusValidator;
import feign.FeignException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class FundTransferServiceImpl implements FundTransferService {

    private static final String ENTITY_NAME = "FundTransfer";

    private final FundTransferRepository transferRepository;
    private final TransferStatusHistoryRepository historyRepository;
    private final TransferMapper transferMapper;
    private final TransferStatusValidator statusValidator;
    private final BeneficiaryClient beneficiaryClient;
    private final ApprovalClient approvalClient;

    @Override
    @Transactional(noRollbackFor = BeneficiaryNotActiveException.class)
    public TransferResponse initiateTransfer(InitiateTransferRequest request) {
        FundTransfer transfer = FundTransfer.builder()
                .transferRefNo(generateTransferRefNo())
                .customerId(request.getCustomerId())
                .beneficiaryId(request.getBeneficiaryId())
                .amount(request.getAmount())
                .currency(request.getCurrency())
                .remarks(request.getRemarks())
                .status(TransferStatus.INITIATED)
                .build();
        transfer = transferRepository.save(transfer);
        recordHistory(transfer, null, TransferStatus.INITIATED, "SYSTEM", "Transfer initiated");

        BeneficiaryValidationResponse validation = validateBeneficiaryViaFeign(request.getBeneficiaryId());

        if (validation.isValid()) {
            applyStatusChange(transfer, TransferStatus.VALIDATED, "SYSTEM",
                    "Beneficiary validated as ACTIVE");
            return transferMapper.toResponse(transfer);
        } else {
            applyStatusChange(transfer, TransferStatus.FAILED, "SYSTEM",
                    "Beneficiary validation failed: " + validation.getMessage());
            throw new BeneficiaryNotActiveException(request.getBeneficiaryId());
        }
    }

    @Override
    public TransferResponse submitForApproval(Long transferId) {
        FundTransfer transfer = findTransferOrThrow(transferId);

        // Validator naturally rejects submission from any status other than VALIDATED
        statusValidator.validateTransition(transfer.getStatus(), TransferStatus.PENDING_APPROVAL);

        CreateApprovalRequestDto approvalRequest = CreateApprovalRequestDto.builder()
                .transferId(transfer.getId())
                .transferRefNo(transfer.getTransferRefNo())
                .customerId(transfer.getCustomerId())
                .amount(transfer.getAmount())
                .currency(transfer.getCurrency())
                .build();

        try {
            ApiResponse<?> response = approvalClient.createApprovalRequest(approvalRequest);
            log.info("Approval request created for transfer [{}]: {}", transfer.getTransferRefNo(), response);
        } catch (FeignException ex) {
            log.error("Failed to submit transfer [{}] to approval-service", transfer.getTransferRefNo(), ex);
            throw new DownstreamServiceException("approval-service", ex.getMessage());
        }

        applyStatusChange(transfer, TransferStatus.PENDING_APPROVAL, "SYSTEM",
                "Submitted to approval-service for checker action");
        return transferMapper.toResponse(transfer);
    }

    @Override
    @Transactional(readOnly = true)
    public TransferResponse getTransferById(Long id) {
        return transferMapper.toResponse(findTransferOrThrow(id));
    }

    @Override
    @Transactional(readOnly = true)
    public TransferResponse getTransferByRefNo(String transferRefNo) {
        FundTransfer transfer = transferRepository.findByTransferRefNo(transferRefNo)
                .orElseThrow(() -> new ResourceNotFoundException(ENTITY_NAME, transferRefNo));
        return transferMapper.toResponse(transfer);
    }

    @Override
    @Transactional(readOnly = true)
    public List<TransferResponse> getTransferHistoryByCustomer(Long customerId) {
        return transferRepository.findByCustomerIdOrderByCreatedAtDesc(customerId).stream()
                .map(transferMapper::toResponse)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<TransferStatusHistoryResponse> getStatusHistory(Long transferId) {
        if (!transferRepository.existsById(transferId)) {
            throw new ResourceNotFoundException(ENTITY_NAME, transferId);
        }
        return historyRepository.findByTransferIdOrderByChangedAtAsc(transferId).stream()
                .map(transferMapper::toHistoryResponse)
                .toList();
    }

    @Override
    public TransferResponse updateStatus(Long transferId, UpdateTransferStatusRequest request) {
        FundTransfer transfer = findTransferOrThrow(transferId);
        applyStatusChange(transfer, request.getNewStatus(), request.getChangedBy(), request.getRemarks());
        return transferMapper.toResponse(transfer);
    }

    @Override
    public TransferResponse resubmitTransfer(Long transferId, ResubmitTransferRequest request) {
        FundTransfer transfer = findTransferOrThrow(transferId);

        // Validator ensures only REJECTED or SENT_BACK transfers may resubmit
        if (transfer.getStatus() != TransferStatus.REJECTED && transfer.getStatus() != TransferStatus.SENT_BACK) {
            throw new InvalidStateTransitionException(transfer.getStatus().name(), "RESUBMIT");
        }

        transfer.setAmount(request.getAmount());
        transfer.setRemarks(request.getRemarks());
        transferRepository.save(transfer);

        CreateApprovalRequestDto approvalRequest = CreateApprovalRequestDto.builder()
                .transferId(transfer.getId())
                .transferRefNo(transfer.getTransferRefNo())
                .customerId(transfer.getCustomerId())
                .amount(transfer.getAmount())
                .currency(transfer.getCurrency())
                .build();

        try {
            approvalClient.createApprovalRequest(approvalRequest);
        } catch (FeignException ex) {
            log.error("Failed to resubmit transfer [{}] to approval-service", transfer.getTransferRefNo(), ex);
            throw new DownstreamServiceException("approval-service", ex.getMessage());
        }

        applyStatusChange(transfer, TransferStatus.PENDING_APPROVAL, "SYSTEM",
                "Transfer modified and resubmitted for approval");
        return transferMapper.toResponse(transfer);
    }

    // -------------------------------------------------------------------
    // Internal helpers
    // -------------------------------------------------------------------

    private BeneficiaryValidationResponse validateBeneficiaryViaFeign(Long beneficiaryId) {
        try {
            ApiResponse<BeneficiaryValidationResponse> response =
                    beneficiaryClient.validateBeneficiary(beneficiaryId);
            return response.getData();
        } catch (FeignException.NotFound ex) {
            throw new ResourceNotFoundException("Beneficiary", beneficiaryId);
        } catch (FeignException ex) {
            log.error("Failed to validate beneficiary [{}] via beneficiary-service", beneficiaryId, ex);
            throw new DownstreamServiceException("beneficiary-service", ex.getMessage());
        }
    }

    /** Validates the transition, applies it, persists, and writes the audit history record. */
    private void applyStatusChange(FundTransfer transfer, TransferStatus newStatus, String changedBy, String remarks) {
        TransferStatus oldStatus = transfer.getStatus();
        statusValidator.validateTransition(oldStatus, newStatus);
        transfer.setStatus(newStatus);
        transferRepository.save(transfer);
        recordHistory(transfer, oldStatus, newStatus, changedBy, remarks);
    }

    private void recordHistory(FundTransfer transfer, TransferStatus oldStatus, TransferStatus newStatus,
                                String changedBy, String remarks) {
        TransferStatusHistory history = TransferStatusHistory.builder()
                .transfer(transfer)
                .oldStatus(oldStatus)
                .newStatus(newStatus)
                .changedAt(LocalDateTime.now())
                .changedBy(changedBy)
                .remarks(remarks)
                .build();
        historyRepository.save(history);
    }

    private FundTransfer findTransferOrThrow(Long id) {
        return transferRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(ENTITY_NAME, id));
    }

    private String generateTransferRefNo() {
        return "TXN-" + UUID.randomUUID().toString().replace("-", "").substring(0, 16).toUpperCase();
    }
}
