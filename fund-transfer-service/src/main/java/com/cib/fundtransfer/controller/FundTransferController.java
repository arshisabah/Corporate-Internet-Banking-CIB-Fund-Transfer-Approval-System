package com.cib.fundtransfer.controller;

import com.cib.common.dto.ApiResponse;
import com.cib.fundtransfer.dto.request.InitiateTransferRequest;
import com.cib.fundtransfer.dto.request.ResubmitTransferRequest;
import com.cib.fundtransfer.dto.request.UpdateTransferStatusRequest;
import com.cib.fundtransfer.dto.response.TransferResponse;
import com.cib.fundtransfer.dto.response.TransferStatusHistoryResponse;
import com.cib.fundtransfer.service.FundTransferService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/transfers")
@RequiredArgsConstructor
@Tag(name = "Fund Transfer", description = "Initiate, submit, track and review the history of fund transfers")
public class FundTransferController {

    private final FundTransferService fundTransferService;

    @PostMapping
    @Operation(summary = "Initiate a fund transfer (validates beneficiary is ACTIVE)")
    public ResponseEntity<ApiResponse<TransferResponse>> initiateTransfer(
            @Valid @RequestBody InitiateTransferRequest request) {
        TransferResponse response = fundTransferService.initiateTransfer(request);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(ApiResponse.success(response, "Transfer initiated and beneficiary validated successfully"));
    }

    @PostMapping("/{id}/submit")
    @Operation(summary = "Submit a VALIDATED transfer to approval-service for checker action")
    public ResponseEntity<ApiResponse<TransferResponse>> submitForApproval(@PathVariable Long id) {
        TransferResponse response = fundTransferService.submitForApproval(id);
        return ResponseEntity.ok(ApiResponse.success(response, "Transfer submitted for approval"));
    }

    @PostMapping("/{id}/resubmit")
    @Operation(summary = "Modify and resubmit a REJECTED or SENT_BACK transfer")
    public ResponseEntity<ApiResponse<TransferResponse>> resubmitTransfer(
            @PathVariable Long id, @Valid @RequestBody ResubmitTransferRequest request) {
        TransferResponse response = fundTransferService.resubmitTransfer(id, request);
        return ResponseEntity.ok(ApiResponse.success(response, "Transfer modified and resubmitted for approval"));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get a transfer by ID")
    public ResponseEntity<ApiResponse<TransferResponse>> getTransferById(@PathVariable Long id) {
        return ResponseEntity.ok(ApiResponse.success(fundTransferService.getTransferById(id)));
    }

    @GetMapping("/search")
    @Operation(summary = "Get a transfer by its reference number")
    public ResponseEntity<ApiResponse<TransferResponse>> getTransferByRefNo(
            @RequestParam String transferRefNo) {
        return ResponseEntity.ok(ApiResponse.success(fundTransferService.getTransferByRefNo(transferRefNo)));
    }

    @GetMapping("/{id}/status")
    @Operation(summary = "Track the current status of a transfer")
    public ResponseEntity<ApiResponse<TransferResponse>> getTransferStatus(@PathVariable Long id) {
        return ResponseEntity.ok(ApiResponse.success(fundTransferService.getTransferById(id)));
    }

    @GetMapping("/{id}/status-history")
    @Operation(summary = "View the full audited status change history of a transfer")
    public ResponseEntity<ApiResponse<List<TransferStatusHistoryResponse>>> getStatusHistory(
            @PathVariable Long id) {
        return ResponseEntity.ok(ApiResponse.success(fundTransferService.getStatusHistory(id)));
    }

    @GetMapping("/by-customer/{customerId}")
    @Operation(summary = "View transfer history for a customer")
    public ResponseEntity<ApiResponse<List<TransferResponse>>> getTransferHistory(
            @PathVariable Long customerId) {
        return ResponseEntity.ok(ApiResponse.success(fundTransferService.getTransferHistoryByCustomer(customerId)));
    }

    /**
     * Internal callback endpoint invoked by approval-service (via its own
     * Feign client) after a checker performs APPROVE / REJECT / SEND_BACK,
     * or after execution completes. Not intended for direct end-user use -
     * in a production deployment this would be secured with a service-to-service
     * auth mechanism (mTLS or an internal API key) at the Gateway/network layer.
     */
    @PatchMapping("/{id}/status")
    @Operation(summary = "[Internal] Update transfer status - invoked by approval-service")
    public ResponseEntity<ApiResponse<TransferResponse>> updateStatus(
            @PathVariable Long id, @Valid @RequestBody UpdateTransferStatusRequest request) {
        TransferResponse response = fundTransferService.updateStatus(id, request);
        return ResponseEntity.ok(ApiResponse.success(response, "Transfer status updated successfully"));
    }
}
