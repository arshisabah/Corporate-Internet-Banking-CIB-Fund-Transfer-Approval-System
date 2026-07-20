package com.cib.customer.service;

import com.cib.customer.dto.request.UserCreateRequest;
import com.cib.customer.dto.request.UserUpdateRequest;
import com.cib.customer.dto.response.UserResponse;

import java.util.List;

public interface CorporateUserService {

    UserResponse createUser(UserCreateRequest request);

    UserResponse getUserById(Long id);

    List<UserResponse> getUsersByCustomerId(Long customerId);

    UserResponse updateUser(Long id, UserUpdateRequest request);

    void deleteUser(Long id);
}
