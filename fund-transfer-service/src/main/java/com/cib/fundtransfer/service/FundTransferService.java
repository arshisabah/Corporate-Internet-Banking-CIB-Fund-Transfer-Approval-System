package com.cib.fundtransfer.service;

import com.cib.fundtransfer.dto.request.InitiateTransferRequest;
import com.cib.fundtransfer.dto.request.ResubmitTransferRequest;
import com.cib.fundtransfer.dto.request.UpdateTransferStatusRequest;
import com.cib.fundtransfer.dto.response.TransferResponse;
import com.cib.fundtransfer.dto.response.TransferStatusHistoryResponse;

import java.util.List;

public interface FundTransferService {

    /**
     * Creates a transfer and immediately validates the beneficiary via
     * beneficiary-service (Feign). Always persists the transfer, even on
     * validation failure (status=FAILED), to preserve a complete audit trail.
     */
    TransferResponse initiateTransfer(InitiateTransferRequest request);

    /**
     * Explicit second step: submits a VALIDATED transfer to approval-service
     * for checker action, transitioning it to PENDING_APPROVAL.
     */
    TransferResponse submitForApproval(Long transferId);

    TransferResponse getTransferById(Long id);

    TransferResponse getTransferByRefNo(String transferRefNo);

    List<TransferResponse> getTransferHistoryByCustomer(Long customerId);

    List<TransferStatusHistoryResponse> getStatusHistory(Long transferId);

    /**
     * Applies a status change requested by approval-service (or any
     * authorized caller) after validating the transition is legal.
     * Writes a TransferStatusHistory audit record for every change.
     */
    TransferResponse updateStatus(Long transferId, UpdateTransferStatusRequest request);

    /**
     * Modifies and resubmits a REJECTED or SENT_BACK transfer, moving it
     * back to PENDING_APPROVAL and re-notifying approval-service.
     */
    TransferResponse resubmitTransfer(Long transferId, ResubmitTransferRequest request);
}
