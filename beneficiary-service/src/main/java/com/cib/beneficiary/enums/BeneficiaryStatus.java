package com.cib.beneficiary.enums;

/**
 * Lifecycle status of a beneficiary. Enforces the core business rule:
 * "Only ACTIVE beneficiaries can receive fund transfers." Any transfer
 * targeting a beneficiary in INACTIVE or BLOCKED status must be rejected
 * by fund-transfer-service before submission to approval.
 */
public enum BeneficiaryStatus {
    ACTIVE,
    INACTIVE,
    BLOCKED
}
