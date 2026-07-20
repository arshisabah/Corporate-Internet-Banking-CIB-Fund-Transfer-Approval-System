package com.cib.approval.service.impl;

import com.cib.approval.client.FundTransferClient;
import com.cib.approval.client.dto.UpdateTransferStatusRequest;
import com.cib.approval.dto.request.ApprovalActionRequest;
import com.cib.approval.dto.request.CreateApprovalRequestDto;
import com.cib.approval.dto.response.ApprovalHistoryResponse;
import com.cib.approval.dto.response.ApprovalRequestCreatedResponse;
import com.cib.approval.dto.response.ApprovalResponse;
import com.cib.approval.dto.response.AuditTrailResponse;
import com.cib.approval.entity.ApprovalAuditTrail;
import com.cib.approval.entity.ApprovalHistory;
import com.cib.approval.entity.ApprovalRequest;
import com.cib.approval.enums.ApprovalAction;
import com.cib.approval.enums.ApprovalStatus;
import com.cib.approval.mapper.ApprovalMapper;
import com.cib.approval.repository.ApprovalAuditTrailRepository;
import com.cib.approval.repository.ApprovalHistoryRepository;
import com.cib.approval.repository.ApprovalRequestRepository;
import com.cib.approval.service.ApprovalService;
import com.cib.common.enums.ErrorCode;
import com.cib.common.exception.BusinessException;
import com.cib.common.exception.DownstreamServiceException;
import com.cib.common.exception.InvalidStateTransitionException;
import com.cib.common.exception.ResourceNotFoundException;
import feign.FeignException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class ApprovalServiceImpl implements ApprovalService {

    private static final String ENTITY_NAME = "ApprovalRequest";

    private final ApprovalRequestRepository approvalRequestRepository;
    private final ApprovalHistoryRepository approvalHistoryRepository;
    private final ApprovalAuditTrailRepository auditTrailRepository;
    private final ApprovalMapper approvalMapper;
    private final FundTransferClient fundTransferClient;

    @Override
    public ApprovalRequestCreatedResponse createApprovalRequest(CreateApprovalRequestDto request) {
        ApprovalRequest entity = ApprovalRequest.builder()
                .transferId(request.getTransferId())
                .transferRefNo(request.getTransferRefNo())
                .customerId(request.getCustomerId())
                .amount(request.getAmount())
                .currency(request.getCurrency())
                .status(ApprovalStatus.PENDING)
                .build();
        ApprovalRequest saved = approvalRequestRepository.save(entity);

        writeAuditTrail(saved.getTransferId(), "REQUEST_CREATED", "SYSTEM",
                "Approval request created for transfer " + saved.getTransferRefNo());

        return ApprovalRequestCreatedResponse.builder()
                .approvalId(saved.getId())
                .transferId(saved.getTransferId())
                .status(saved.getStatus())
                .build();
    }

    @Override
    public ApprovalResponse approve(Long approvalRequestId, ApprovalActionRequest request) {
        return performAction(approvalRequestId, ApprovalAction.APPROVE, ApprovalStatus.APPROVED,
                "APPROVED", request);
    }

    @Override
    public ApprovalResponse reject(Long approvalRequestId, ApprovalActionRequest request) {
        requireRemarks(request, "Rejection reason (remarks) is required");
        return performAction(approvalRequestId, ApprovalAction.REJECT, ApprovalStatus.REJECTED,
                "REJECTED", request);
    }

    @Override
    public ApprovalResponse sendBack(Long approvalRequestId, ApprovalActionRequest request) {
        requireRemarks(request, "Send-back reason (remarks) is required so the maker knows what to modify");
        return performAction(approvalRequestId, ApprovalAction.SEND_BACK, ApprovalStatus.SENT_BACK,
                "SENT_BACK", request);
    }

    @Override
    @Transactional(readOnly = true)
    public ApprovalResponse getApprovalById(Long id) {
        return approvalMapper.toResponse(findApprovalOrThrow(id));
    }

    @Override
    @Transactional(readOnly = true)
    public List<ApprovalResponse> getPendingApprovals() {
        return approvalRequestRepository.findByStatus(ApprovalStatus.PENDING).stream()
                .map(approvalMapper::toResponse)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<ApprovalHistoryResponse> getHistory(Long approvalRequestId) {
        if (!approvalRequestRepository.existsById(approvalRequestId)) {
            throw new ResourceNotFoundException(ENTITY_NAME, approvalRequestId);
        }
        return approvalHistoryRepository.findByApprovalRequestIdOrderByActionDateAsc(approvalRequestId).stream()
                .map(approvalMapper::toHistoryResponse)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<AuditTrailResponse> getAuditTrail(Long transferId) {
        return auditTrailRepository.findByTransferIdOrderByPerformedAtAsc(transferId).stream()
                .map(approvalMapper::toAuditTrailResponse)
                .toList();
    }

    // -------------------------------------------------------------------
    // Internal helpers
    // -------------------------------------------------------------------

    /**
     * Shared logic for all three checker actions: validates the request is
     * still PENDING, updates status, writes both the history and audit
     * trail records, then calls back into fund-transfer-service to sync
     * TransferStatus. transferTargetStatus is sent as a plain string since
     * TransferStatus is owned exclusively by fund-transfer-service.
     */
    private ApprovalResponse performAction(Long approvalRequestId, ApprovalAction action,
                                            ApprovalStatus newApprovalStatus, String transferTargetStatus,
                                            ApprovalActionRequest request) {
        ApprovalRequest approvalRequest = findApprovalOrThrow(approvalRequestId);

        if (approvalRequest.getStatus() != ApprovalStatus.PENDING) {
            throw new InvalidStateTransitionException(approvalRequest.getStatus().name(), action.name());
        }

        approvalRequest.setStatus(newApprovalStatus);
        approvalRequestRepository.save(approvalRequest);

        ApprovalHistory history = ApprovalHistory.builder()
                .approvalRequest(approvalRequest)
                .action(action)
                .approverId(request.getApproverId())
                .remarks(request.getRemarks())
                .actionDate(LocalDateTime.now())
                .build();
        approvalHistoryRepository.save(history);

        writeAuditTrail(approvalRequest.getTransferId(), action.name(), request.getApproverId(),
                request.getRemarks());

        notifyFundTransferService(approvalRequest.getTransferId(), transferTargetStatus,
                request.getApproverId(), request.getRemarks());

        return approvalMapper.toResponse(approvalRequest);
    }

    private void notifyFundTransferService(Long transferId, String newStatus, String changedBy, String remarks) {
        UpdateTransferStatusRequest statusRequest = UpdateTransferStatusRequest.builder()
                .newStatus(newStatus)
                .changedBy(changedBy)
                .remarks(remarks)
                .build();
        try {
            fundTransferClient.updateTransferStatus(transferId, statusRequest);
        } catch (FeignException ex) {
            log.error("Failed to sync status back to fund-transfer-service for transfer [{}]", transferId, ex);
            throw new DownstreamServiceException("fund-transfer-service", ex.getMessage());
        }
    }

    private void writeAuditTrail(Long transferId, String action, String performedBy, String details) {
        ApprovalAuditTrail entry = ApprovalAuditTrail.builder()
                .transferId(transferId)
                .action(action)
                .performedBy(performedBy)
                .performedAt(LocalDateTime.now())
                .details(details)
                .build();
        auditTrailRepository.save(entry);
    }

    private void requireRemarks(ApprovalActionRequest request, String message) {
        if (!StringUtils.hasText(request.getRemarks())) {
            throw new BusinessException(ErrorCode.VALIDATION_FAILED, message);
        }
    }

    private ApprovalRequest findApprovalOrThrow(Long id) {
        return approvalRequestRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(ENTITY_NAME, id));
    }
}
