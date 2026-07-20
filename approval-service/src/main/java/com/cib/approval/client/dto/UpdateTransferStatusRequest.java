package com.cib.approval.client.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Mirrors fund-transfer-service's UpdateTransferStatusRequest contract
 * (PATCH /api/v1/transfers/{id}/status). newStatus is sent as a String
 * rather than a shared enum type to keep the two services' TransferStatus
 * enums independently owned - each service is free to add its own internal
 * states without breaking the other.
 */
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UpdateTransferStatusRequest {
    private String newStatus;
    private String changedBy;
    private String remarks;
}
