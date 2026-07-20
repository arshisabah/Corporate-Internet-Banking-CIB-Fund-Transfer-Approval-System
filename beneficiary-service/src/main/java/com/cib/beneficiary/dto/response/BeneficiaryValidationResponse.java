package com.cib.beneficiary.dto.response;

import com.cib.beneficiary.enums.BeneficiaryStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Lightweight response contract returned by GET /api/v1/beneficiaries/{id}/validate.
 * This is the exact payload fund-transfer-service's BeneficiaryClient (Feign)
 * will consume to decide whether a transfer may proceed. Kept intentionally
 * minimal and stable since it is a cross-service API contract.
 */
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BeneficiaryValidationResponse {

    private Long beneficiaryId;
    private Long customerId;
    private boolean valid;
    private BeneficiaryStatus status;
    private String message;
}
