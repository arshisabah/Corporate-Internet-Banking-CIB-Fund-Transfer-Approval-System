package com.cib.fundtransfer.client.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Mirrors beneficiary-service's BeneficiaryValidationResponse contract
 * (GET /api/v1/beneficiaries/{id}/validate). Kept as a local, minimal copy
 * rather than a shared dependency to preserve service independence -
 * each service can evolve its own DTOs as long as this contract's shape
 * is preserved on both sides.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class BeneficiaryValidationResponse {
    private Long beneficiaryId;
    private Long customerId;
    private boolean valid;
    private String status;
    private String message;
}
