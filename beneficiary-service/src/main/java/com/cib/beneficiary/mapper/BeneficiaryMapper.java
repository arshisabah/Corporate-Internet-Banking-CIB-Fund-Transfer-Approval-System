package com.cib.beneficiary.mapper;

import com.cib.beneficiary.dto.request.BeneficiaryCreateRequest;
import com.cib.beneficiary.dto.request.BeneficiaryUpdateRequest;
import com.cib.beneficiary.dto.response.BeneficiaryResponse;
import com.cib.beneficiary.entity.Beneficiary;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface BeneficiaryMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "status", constant = "ACTIVE")
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "createdBy", ignore = true)
    @Mapping(target = "updatedBy", ignore = true)
    Beneficiary toEntity(BeneficiaryCreateRequest request);

    BeneficiaryResponse toResponse(Beneficiary entity);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "customerId", ignore = true)
    @Mapping(target = "accountNumber", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "createdBy", ignore = true)
    @Mapping(target = "updatedBy", ignore = true)
    void updateEntityFromRequest(BeneficiaryUpdateRequest request, @MappingTarget Beneficiary entity);
}
