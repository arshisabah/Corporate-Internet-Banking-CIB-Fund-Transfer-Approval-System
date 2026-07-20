package com.cib.customer.mapper;

import com.cib.customer.dto.request.UserCreateRequest;
import com.cib.customer.dto.request.UserUpdateRequest;
import com.cib.customer.dto.response.UserResponse;
import com.cib.customer.entity.CorporateUser;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

/**
 * MapStruct mapper between CorporateUser entity and its request/response DTOs.
 * The 'customer' association is intentionally NOT mapped directly from
 * UserCreateRequest.customerId - the service layer resolves and validates
 * the CorporateCustomer entity first, then sets it explicitly, keeping
 * referential integrity checks (does the customer exist? is it ACTIVE?)
 * in the service layer rather than the mapper.
 */
@Mapper(componentModel = "spring")
public interface UserMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "customer", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "createdBy", ignore = true)
    @Mapping(target = "updatedBy", ignore = true)
    CorporateUser toEntity(UserCreateRequest request);

    @Mapping(target = "customerId", source = "customer.id")
    @Mapping(target = "customerName", source = "customer.companyName")
    UserResponse toResponse(CorporateUser entity);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "customer", ignore = true)
    @Mapping(target = "username", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "createdBy", ignore = true)
    @Mapping(target = "updatedBy", ignore = true)
    void updateEntityFromRequest(UserUpdateRequest request, @MappingTarget CorporateUser entity);
}
