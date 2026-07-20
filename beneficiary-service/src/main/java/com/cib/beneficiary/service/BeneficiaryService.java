package com.cib.beneficiary.service;

import com.cib.beneficiary.dto.request.BeneficiaryCreateRequest;
import com.cib.beneficiary.dto.request.BeneficiaryUpdateRequest;
import com.cib.beneficiary.dto.response.BeneficiaryResponse;
import com.cib.beneficiary.dto.response.BeneficiaryValidationResponse;

import java.util.List;

public interface BeneficiaryService {

    BeneficiaryResponse createBeneficiary(BeneficiaryCreateRequest request);

    BeneficiaryResponse getBeneficiaryById(Long id);

    List<BeneficiaryResponse> getBeneficiariesByCustomerId(Long customerId);

    BeneficiaryResponse updateBeneficiary(Long id, BeneficiaryUpdateRequest request);

    void deleteBeneficiary(Long id);

    /**
     * Validates whether a beneficiary is eligible to receive a fund transfer
     * (i.e. exists and is ACTIVE). Never throws for a "not eligible" case -
     * that distinction is communicated via the response's 'valid' flag so
     * fund-transfer-service can handle it as a normal business decision
     * rather than an exceptional Feign failure. Only genuinely missing
     * beneficiaries throw ResourceNotFoundException (404).
     */
    BeneficiaryValidationResponse validateBeneficiary(Long id);
}
