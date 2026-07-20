package com.cib.approval.dto.response;

import com.cib.approval.enums.ApprovalStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Minimal response returned by POST /api/v1/approvals/requests - matches
 * the ApprovalRequestResponse shape fund-transfer-service's ApprovalClient expects.
 */
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ApprovalRequestCreatedResponse {
    private Long approvalId;
    private Long transferId;
    private ApprovalStatus status;
}
