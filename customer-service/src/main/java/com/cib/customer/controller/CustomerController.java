package com.cib.customer.controller;

import com.cib.common.dto.ApiResponse;
import com.cib.customer.dto.request.CustomerCreateRequest;
import com.cib.customer.dto.request.CustomerUpdateRequest;
import com.cib.customer.dto.response.CustomerResponse;
import com.cib.customer.service.CustomerService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/customers")
@RequiredArgsConstructor
@Tag(name = "Corporate Customer", description = "Manage corporate customer (company) records")
public class CustomerController {

    private final CustomerService customerService;

    @PostMapping
    @Operation(summary = "Onboard a new corporate customer")
    public ResponseEntity<ApiResponse<CustomerResponse>> createCustomer(
            @Valid @RequestBody CustomerCreateRequest request) {
        CustomerResponse response = customerService.createCustomer(request);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(ApiResponse.success(response, "Corporate customer created successfully"));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get a corporate customer by ID")
    public ResponseEntity<ApiResponse<CustomerResponse>> getCustomerById(@PathVariable Long id) {
        return ResponseEntity.ok(ApiResponse.success(customerService.getCustomerById(id)));
    }

    @GetMapping("/search")
    @Operation(summary = "Get a corporate customer by CIF number")
    public ResponseEntity<ApiResponse<CustomerResponse>> getCustomerByCif(
            @RequestParam String cifNumber) {
        return ResponseEntity.ok(ApiResponse.success(customerService.getCustomerByCifNumber(cifNumber)));
    }

    @GetMapping
    @Operation(summary = "List all corporate customers")
    public ResponseEntity<ApiResponse<List<CustomerResponse>>> getAllCustomers() {
        return ResponseEntity.ok(ApiResponse.success(customerService.getAllCustomers()));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update a corporate customer")
    public ResponseEntity<ApiResponse<CustomerResponse>> updateCustomer(
            @PathVariable Long id, @Valid @RequestBody CustomerUpdateRequest request) {
        CustomerResponse response = customerService.updateCustomer(id, request);
        return ResponseEntity.ok(ApiResponse.success(response, "Corporate customer updated successfully"));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete a corporate customer")
    public ResponseEntity<ApiResponse<Void>> deleteCustomer(@PathVariable Long id) {
        customerService.deleteCustomer(id);
        return ResponseEntity.ok(ApiResponse.success(null, "Corporate customer deleted successfully"));
    }
}
