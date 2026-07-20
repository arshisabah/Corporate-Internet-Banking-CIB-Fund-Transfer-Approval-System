package com.cib.approval.service;

import com.cib.approval.dto.request.ApprovalActionRequest;
import com.cib.approval.dto.request.CreateApprovalRequestDto;
import com.cib.approval.dto.response.ApprovalHistoryResponse;
import com.cib.approval.dto.response.ApprovalRequestCreatedResponse;
import com.cib.approval.dto.response.ApprovalResponse;
import com.cib.approval.dto.response.AuditTrailResponse;

import java.util.List;

public interface ApprovalService {

    /** Called by fund-transfer-service when a transfer is submitted for approval. */
    ApprovalRequestCreatedResponse createApprovalRequest(CreateApprovalRequestDto request);

    ApprovalResponse approve(Long approvalRequestId, ApprovalActionRequest request);

    ApprovalResponse reject(Long approvalRequestId, ApprovalActionRequest request);

    ApprovalResponse sendBack(Long approvalRequestId, ApprovalActionRequest request);

    ApprovalResponse getApprovalById(Long id);

    List<ApprovalResponse> getPendingApprovals();

    List<ApprovalHistoryResponse> getHistory(Long approvalRequestId);

    List<AuditTrailResponse> getAuditTrail(Long transferId);
}
