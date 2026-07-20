package com.cib.customer.mapper;

import com.cib.customer.dto.request.CustomerCreateRequest;
import com.cib.customer.dto.response.CustomerResponse;
import com.cib.customer.entity.CorporateCustomer;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

/**
 * MapStruct mapper between CorporateCustomer entity and its request/response DTOs.
 * componentModel = "spring" registers this as an injectable Spring bean.
 */
@Mapper(componentModel = "spring")
public interface CustomerMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "status", constant = "ACTIVE")
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "createdBy", ignore = true)
    @Mapping(target = "updatedBy", ignore = true)
    CorporateCustomer toEntity(CustomerCreateRequest request);

    CustomerResponse toResponse(CorporateCustomer entity);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "cifNumber", ignore = true)
    @Mapping(target = "registrationNumber", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "createdBy", ignore = true)
    @Mapping(target = "updatedBy", ignore = true)
    void updateEntityFromRequest(com.cib.customer.dto.request.CustomerUpdateRequest request,
                                  @MappingTarget CorporateCustomer entity);
}
