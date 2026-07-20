package com.cib.fundtransfer.client;

import com.cib.common.dto.ApiResponse;
import com.cib.fundtransfer.client.dto.ApprovalRequestResponse;
import com.cib.fundtransfer.client.dto.CreateApprovalRequestDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

/**
 * Feign client for approval-service. Used exclusively by the explicit
 * "submit for approval" step (POST /api/v1/transfers/{id}/submit),
 * never during transfer creation itself - see the two-step design
 * decision: creation and validation must succeed independently of
 * approval-service's availability.
 */
@FeignClient(name = "approval-service", path = "/api/v1/approvals")
public interface ApprovalClient {

    @PostMapping("/requests")
    ApiResponse<ApprovalRequestResponse> createApprovalRequest(@RequestBody CreateApprovalRequestDto request);
}
