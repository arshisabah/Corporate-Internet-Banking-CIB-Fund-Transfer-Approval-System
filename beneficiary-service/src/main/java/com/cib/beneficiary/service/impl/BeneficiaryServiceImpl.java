package com.cib.beneficiary.service.impl;

import com.cib.beneficiary.dto.request.BeneficiaryCreateRequest;
import com.cib.beneficiary.dto.request.BeneficiaryUpdateRequest;
import com.cib.beneficiary.dto.response.BeneficiaryResponse;
import com.cib.beneficiary.dto.response.BeneficiaryValidationResponse;
import com.cib.beneficiary.entity.Beneficiary;
import com.cib.beneficiary.enums.BeneficiaryStatus;
import com.cib.beneficiary.mapper.BeneficiaryMapper;
import com.cib.beneficiary.repository.BeneficiaryRepository;
import com.cib.beneficiary.service.BeneficiaryService;
import com.cib.common.exception.DuplicateResourceException;
import com.cib.common.exception.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class BeneficiaryServiceImpl implements BeneficiaryService {

    private static final String ENTITY_NAME = "Beneficiary";

    private final BeneficiaryRepository beneficiaryRepository;
    private final BeneficiaryMapper beneficiaryMapper;

    @Override
    public BeneficiaryResponse createBeneficiary(BeneficiaryCreateRequest request) {
        boolean duplicate = beneficiaryRepository.existsByCustomerIdAndAccountNumberAndBankName(
                request.getCustomerId(), request.getAccountNumber(), request.getBankName());
        if (duplicate) {
            throw new DuplicateResourceException(
                    "A beneficiary with this account number and bank already exists for this customer");
        }

        Beneficiary entity = beneficiaryMapper.toEntity(request);
        Beneficiary saved = beneficiaryRepository.save(entity);
        return beneficiaryMapper.toResponse(saved);
    }

    @Override
    @Transactional(readOnly = true)
    public BeneficiaryResponse getBeneficiaryById(Long id) {
        return beneficiaryMapper.toResponse(findBeneficiaryOrThrow(id));
    }

    @Override
    @Transactional(readOnly = true)
    public List<BeneficiaryResponse> getBeneficiariesByCustomerId(Long customerId) {
        return beneficiaryRepository.findByCustomerId(customerId).stream()
                .map(beneficiaryMapper::toResponse)
                .toList();
    }

    @Override
    public BeneficiaryResponse updateBeneficiary(Long id, BeneficiaryUpdateRequest request) {
        Beneficiary existing = findBeneficiaryOrThrow(id);
        beneficiaryMapper.updateEntityFromRequest(request, existing);
        Beneficiary updated = beneficiaryRepository.save(existing);
        return beneficiaryMapper.toResponse(updated);
    }

    @Override
    public void deleteBeneficiary(Long id) {
        Beneficiary existing = findBeneficiaryOrThrow(id);
        beneficiaryRepository.delete(existing);
    }

    @Override
    @Transactional(readOnly = true)
    public BeneficiaryValidationResponse validateBeneficiary(Long id) {
        Beneficiary beneficiary = findBeneficiaryOrThrow(id);
        boolean isActive = beneficiary.getStatus() == BeneficiaryStatus.ACTIVE;

        return BeneficiaryValidationResponse.builder()
                .beneficiaryId(beneficiary.getId())
                .customerId(beneficiary.getCustomerId())
                .valid(isActive)
                .status(beneficiary.getStatus())
                .message(isActive
                        ? "Beneficiary is ACTIVE and eligible to receive transfers"
                        : "Beneficiary is not ACTIVE (current status: " + beneficiary.getStatus() + ")")
                .build();
    }

    private Beneficiary findBeneficiaryOrThrow(Long id) {
        return beneficiaryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(ENTITY_NAME, id));
    }
}
