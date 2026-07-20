package com.cib.common.exception;

import com.cib.common.enums.ErrorCode;

/**
 * Thrown by FundTransferService when the target beneficiary is not in ACTIVE
 * status. Enforces the business rule: "Only ACTIVE beneficiaries can receive transfers."
 */
public class BeneficiaryNotActiveException extends BusinessException {

    public BeneficiaryNotActiveException(Long beneficiaryId) {
        super(ErrorCode.BENEFICIARY_NOT_ACTIVE,
                String.format("Beneficiary [id=%d] is not ACTIVE. Transfer cannot proceed.", beneficiaryId));
    }
}
