package com.cib.customer.service.impl;

import com.cib.common.exception.DuplicateResourceException;
import com.cib.common.exception.ResourceNotFoundException;
import com.cib.customer.dto.request.CustomerCreateRequest;
import com.cib.customer.dto.request.CustomerUpdateRequest;
import com.cib.customer.dto.response.CustomerResponse;
import com.cib.customer.entity.CorporateCustomer;
import com.cib.customer.mapper.CustomerMapper;
import com.cib.customer.repository.CorporateCustomerRepository;
import com.cib.customer.service.CustomerService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Implements corporate customer management. Constructor-injected dependencies
 * (via Lombok @RequiredArgsConstructor) keep this class trivially testable
 * with mocked collaborators.
 */
@Service
@RequiredArgsConstructor
@Transactional
public class CustomerServiceImpl implements CustomerService {

    private static final String ENTITY_NAME = "CorporateCustomer";

    private final CorporateCustomerRepository customerRepository;
    private final CustomerMapper customerMapper;

    @Override
    public CustomerResponse createCustomer(CustomerCreateRequest request) {
        if (customerRepository.existsByCifNumber(request.getCifNumber())) {
            throw new DuplicateResourceException(
                    "A customer with CIF number '" + request.getCifNumber() + "' already exists");
        }
        if (customerRepository.existsByRegistrationNumber(request.getRegistrationNumber())) {
            throw new DuplicateResourceException(
                    "A customer with registration number '" + request.getRegistrationNumber() + "' already exists");
        }

        CorporateCustomer entity = customerMapper.toEntity(request);
        CorporateCustomer saved = customerRepository.save(entity);
        return customerMapper.toResponse(saved);
    }

    @Override
    @Transactional(readOnly = true)
    public CustomerResponse getCustomerById(Long id) {
        return customerMapper.toResponse(findCustomerOrThrow(id));
    }

    @Override
    @Transactional(readOnly = true)
    public CustomerResponse getCustomerByCifNumber(String cifNumber) {
        CorporateCustomer customer = customerRepository.findByCifNumber(cifNumber)
                .orElseThrow(() -> new ResourceNotFoundException(ENTITY_NAME, cifNumber));
        return customerMapper.toResponse(customer);
    }

    @Override
    @Transactional(readOnly = true)
    public List<CustomerResponse> getAllCustomers() {
        return customerRepository.findAll().stream()
                .map(customerMapper::toResponse)
                .toList();
    }

    @Override
    public CustomerResponse updateCustomer(Long id, CustomerUpdateRequest request) {
        CorporateCustomer existing = findCustomerOrThrow(id);
        customerMapper.updateEntityFromRequest(request, existing);
        CorporateCustomer updated = customerRepository.save(existing);
        return customerMapper.toResponse(updated);
    }

    @Override
    public void deleteCustomer(Long id) {
        CorporateCustomer existing = findCustomerOrThrow(id);
        customerRepository.delete(existing);
    }

    private CorporateCustomer findCustomerOrThrow(Long id) {
        return customerRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(ENTITY_NAME, id));
    }
}
