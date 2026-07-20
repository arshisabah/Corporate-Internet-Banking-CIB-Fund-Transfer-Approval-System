package com.cib.approval.controller;

import com.cib.approval.dto.request.ApprovalActionRequest;
import com.cib.approval.dto.request.CreateApprovalRequestDto;
import com.cib.approval.dto.response.ApprovalHistoryResponse;
import com.cib.approval.dto.response.ApprovalRequestCreatedResponse;
import com.cib.approval.dto.response.ApprovalResponse;
import com.cib.approval.dto.response.AuditTrailResponse;
import com.cib.approval.service.ApprovalService;
import com.cib.common.dto.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/approvals")
@RequiredArgsConstructor
@Tag(name = "Approval", description = "Approve, reject, or send back fund transfers; view approval history and audit trail")
public class ApprovalController {

    private final ApprovalService approvalService;

    /**
     * Consumed by fund-transfer-service's ApprovalClient (Feign) when a
     * VALIDATED transfer is submitted for approval.
     */
    @PostMapping("/requests")
    @Operation(summary = "[Internal] Create an approval request - invoked by fund-transfer-service")
    public ResponseEntity<ApiResponse<ApprovalRequestCreatedResponse>> createApprovalRequest(
            @Valid @RequestBody CreateApprovalRequestDto request) {
        ApprovalRequestCreatedResponse response = approvalService.createApprovalRequest(request);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(ApiResponse.success(response, "Approval request created successfully"));
    }

    @PostMapping("/{id}/approve")
    @Operation(summary = "Approve a pending fund transfer")
    public ResponseEntity<ApiResponse<ApprovalResponse>> approve(
            @PathVariable Long id, @Valid @RequestBody ApprovalActionRequest request) {
        ApprovalResponse response = approvalService.approve(id, request);
        return ResponseEntity.ok(ApiResponse.success(response, "Transfer approved successfully"));
    }

    @PostMapping("/{id}/reject")
    @Operation(summary = "Reject a pending fund transfer")
    public ResponseEntity<ApiResponse<ApprovalResponse>> reject(
            @PathVariable Long id, @Valid @RequestBody ApprovalActionRequest request) {
        ApprovalResponse response = approvalService.reject(id, request);
        return ResponseEntity.ok(ApiResponse.success(response, "Transfer rejected"));
    }

    @PostMapping("/{id}/send-back")
    @Operation(summary = "Send a pending fund transfer back for modification")
    public ResponseEntity<ApiResponse<ApprovalResponse>> sendBack(
            @PathVariable Long id, @Valid @RequestBody ApprovalActionRequest request) {
        ApprovalResponse response = approvalService.sendBack(id, request);
        return ResponseEntity.ok(ApiResponse.success(response, "Transfer sent back for modification"));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get an approval request by ID")
    public ResponseEntity<ApiResponse<ApprovalResponse>> getApprovalById(@PathVariable Long id) {
        return ResponseEntity.ok(ApiResponse.success(approvalService.getApprovalById(id)));
    }

    @GetMapping("/pending")
    @Operation(summary = "List all pending approval requests (checker queue)")
    public ResponseEntity<ApiResponse<List<ApprovalResponse>>> getPendingApprovals() {
        return ResponseEntity.ok(ApiResponse.success(approvalService.getPendingApprovals()));
    }

    @GetMapping("/{id}/history")
    @Operation(summary = "View the approval history of a request")
    public ResponseEntity<ApiResponse<List<ApprovalHistoryResponse>>> getHistory(@PathVariable Long id) {
        return ResponseEntity.ok(ApiResponse.success(approvalService.getHistory(id)));
    }

    @GetMapping("/by-transfer/{transferId}/audit-trail")
    @Operation(summary = "View the full compliance audit trail for a transfer")
    public ResponseEntity<ApiResponse<List<AuditTrailResponse>>> getAuditTrail(
            @PathVariable Long transferId) {
        return ResponseEntity.ok(ApiResponse.success(approvalService.getAuditTrail(transferId)));
    }
}
