package com.cib.customer.service;

import com.cib.customer.dto.request.CustomerCreateRequest;
import com.cib.customer.dto.request.CustomerUpdateRequest;
import com.cib.customer.dto.response.CustomerResponse;

import java.util.List;

public interface CustomerService {

    CustomerResponse createCustomer(CustomerCreateRequest request);

    CustomerResponse getCustomerById(Long id);

    CustomerResponse getCustomerByCifNumber(String cifNumber);

    List<CustomerResponse> getAllCustomers();

    CustomerResponse updateCustomer(Long id, CustomerUpdateRequest request);

    void deleteCustomer(Long id);
}
