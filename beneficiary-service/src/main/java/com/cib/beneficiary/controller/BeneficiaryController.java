package com.cib.beneficiary.controller;

import com.cib.beneficiary.dto.request.BeneficiaryCreateRequest;
import com.cib.beneficiary.dto.request.BeneficiaryUpdateRequest;
import com.cib.beneficiary.dto.response.BeneficiaryResponse;
import com.cib.beneficiary.dto.response.BeneficiaryValidationResponse;
import com.cib.beneficiary.service.BeneficiaryService;
import com.cib.common.dto.ApiResponse;
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
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/beneficiaries")
@RequiredArgsConstructor
@Tag(name = "Beneficiary", description = "Manage beneficiaries and validate their eligibility for fund transfers")
public class BeneficiaryController {

    private final BeneficiaryService beneficiaryService;

    @PostMapping
    @Operation(summary = "Register a new beneficiary")
    public ResponseEntity<ApiResponse<BeneficiaryResponse>> createBeneficiary(
            @Valid @RequestBody BeneficiaryCreateRequest request) {
        BeneficiaryResponse response = beneficiaryService.createBeneficiary(request);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(ApiResponse.success(response, "Beneficiary registered successfully"));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get a beneficiary by ID")
    public ResponseEntity<ApiResponse<BeneficiaryResponse>> getBeneficiaryById(@PathVariable Long id) {
        return ResponseEntity.ok(ApiResponse.success(beneficiaryService.getBeneficiaryById(id)));
    }

    @GetMapping("/by-customer/{customerId}")
    @Operation(summary = "List all beneficiaries for a customer")
    public ResponseEntity<ApiResponse<List<BeneficiaryResponse>>> getBeneficiariesByCustomer(
            @PathVariable Long customerId) {
        return ResponseEntity.ok(ApiResponse.success(beneficiaryService.getBeneficiariesByCustomerId(customerId)));
    }

    /**
     * Consumed by fund-transfer-service's BeneficiaryClient (Feign) before
     * any transfer is created. Returns 200 with valid=false for a
     * non-ACTIVE beneficiary (a normal business outcome), and 404 only
     * if the beneficiary does not exist at all.
     */
    @GetMapping("/{id}/validate")
    @Operation(summary = "Validate whether a beneficiary is ACTIVE and eligible to receive transfers")
    public ResponseEntity<ApiResponse<BeneficiaryValidationResponse>> validateBeneficiary(
            @PathVariable Long id) {
        return ResponseEntity.ok(ApiResponse.success(beneficiaryService.validateBeneficiary(id)));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update a beneficiary")
    public ResponseEntity<ApiResponse<BeneficiaryResponse>> updateBeneficiary(
            @PathVariable Long id, @Valid @RequestBody BeneficiaryUpdateRequest request) {
        BeneficiaryResponse response = beneficiaryService.updateBeneficiary(id, request);
        return ResponseEntity.ok(ApiResponse.success(response, "Beneficiary updated successfully"));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete a beneficiary")
    public ResponseEntity<ApiResponse<Void>> deleteBeneficiary(@PathVariable Long id) {
        beneficiaryService.deleteBeneficiary(id);
        return ResponseEntity.ok(ApiResponse.success(null, "Beneficiary deleted successfully"));
    }
}
