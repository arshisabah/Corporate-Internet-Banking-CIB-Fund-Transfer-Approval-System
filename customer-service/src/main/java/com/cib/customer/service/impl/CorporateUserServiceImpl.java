package com.cib.customer.service.impl;

import com.cib.common.exception.DuplicateResourceException;
import com.cib.common.exception.ResourceNotFoundException;
import com.cib.customer.dto.request.UserCreateRequest;
import com.cib.customer.dto.request.UserUpdateRequest;
import com.cib.customer.dto.response.UserResponse;
import com.cib.customer.entity.CorporateCustomer;
import com.cib.customer.entity.CorporateUser;
import com.cib.customer.enums.UserStatus;
import com.cib.customer.mapper.UserMapper;
import com.cib.customer.repository.CorporateCustomerRepository;
import com.cib.customer.repository.CorporateUserRepository;
import com.cib.customer.service.CorporateUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class CorporateUserServiceImpl implements CorporateUserService {

    private static final String USER_ENTITY = "CorporateUser";
    private static final String CUSTOMER_ENTITY = "CorporateCustomer";

    private final CorporateUserRepository userRepository;
    private final CorporateCustomerRepository customerRepository;
    private final UserMapper userMapper;

    @Override
    public UserResponse createUser(UserCreateRequest request) {
        CorporateCustomer customer = customerRepository.findById(request.getCustomerId())
                .orElseThrow(() -> new ResourceNotFoundException(CUSTOMER_ENTITY, request.getCustomerId()));

        if (userRepository.existsByUsername(request.getUsername())) {
            throw new DuplicateResourceException(
                    "Username '" + request.getUsername() + "' is already taken");
        }
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new DuplicateResourceException(
                    "A user with email '" + request.getEmail() + "' already exists");
        }

        CorporateUser entity = userMapper.toEntity(request);
        entity.setCustomer(customer);
        entity.setStatus(UserStatus.ACTIVE);

        CorporateUser saved = userRepository.save(entity);
        return userMapper.toResponse(saved);
    }

    @Override
    @Transactional(readOnly = true)
    public UserResponse getUserById(Long id) {
        return userMapper.toResponse(findUserOrThrow(id));
    }

    @Override
    @Transactional(readOnly = true)
    public List<UserResponse> getUsersByCustomerId(Long customerId) {
        if (!customerRepository.existsById(customerId)) {
            throw new ResourceNotFoundException(CUSTOMER_ENTITY, customerId);
        }
        return userRepository.findByCustomerId(customerId).stream()
                .map(userMapper::toResponse)
                .toList();
    }

    @Override
    public UserResponse updateUser(Long id, UserUpdateRequest request) {
        CorporateUser existing = findUserOrThrow(id);
        userMapper.updateEntityFromRequest(request, existing);
        CorporateUser updated = userRepository.save(existing);
        return userMapper.toResponse(updated);
    }

    @Override
    public void deleteUser(Long id) {
        CorporateUser existing = findUserOrThrow(id);
        userRepository.delete(existing);
    }

    private CorporateUser findUserOrThrow(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(USER_ENTITY, id));
    }
}
