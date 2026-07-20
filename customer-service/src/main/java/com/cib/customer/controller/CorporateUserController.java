package com.cib.customer.controller;

import com.cib.common.dto.ApiResponse;
import com.cib.customer.dto.request.UserCreateRequest;
import com.cib.customer.dto.request.UserUpdateRequest;
import com.cib.customer.dto.response.UserResponse;
import com.cib.customer.service.CorporateUserService;
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
@RequestMapping("/api/v1/corporate-users")
@RequiredArgsConstructor
@Tag(name = "Corporate User", description = "Manage individual users (MAKER/CHECKER/ADMIN) within a corporate customer")
public class CorporateUserController {

    private final CorporateUserService userService;

    @PostMapping
    @Operation(summary = "Create a new corporate user under a customer")
    public ResponseEntity<ApiResponse<UserResponse>> createUser(
            @Valid @RequestBody UserCreateRequest request) {
        UserResponse response = userService.createUser(request);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(ApiResponse.success(response, "Corporate user created successfully"));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get a corporate user by ID")
    public ResponseEntity<ApiResponse<UserResponse>> getUserById(@PathVariable Long id) {
        return ResponseEntity.ok(ApiResponse.success(userService.getUserById(id)));
    }

    @GetMapping("/by-customer/{customerId}")
    @Operation(summary = "List all users belonging to a corporate customer")
    public ResponseEntity<ApiResponse<List<UserResponse>>> getUsersByCustomer(
            @PathVariable Long customerId) {
        return ResponseEntity.ok(ApiResponse.success(userService.getUsersByCustomerId(customerId)));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update a corporate user")
    public ResponseEntity<ApiResponse<UserResponse>> updateUser(
            @PathVariable Long id, @Valid @RequestBody UserUpdateRequest request) {
        UserResponse response = userService.updateUser(id, request);
        return ResponseEntity.ok(ApiResponse.success(response, "Corporate user updated successfully"));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete a corporate user")
    public ResponseEntity<ApiResponse<Void>> deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
        return ResponseEntity.ok(ApiResponse.success(null, "Corporate user deleted successfully"));
    }
}
