package com.cib.approval.enums;

/**
 * Status of an approval request itself (distinct from TransferStatus in
 * fund-transfer-service, though they are kept in sync via the callback
 * to fund-transfer-service's PATCH /{id}/status endpoint).
 */
public enum ApprovalStatus {
    PENDING,
    APPROVED,
    REJECTED,
    SENT_BACK
}
