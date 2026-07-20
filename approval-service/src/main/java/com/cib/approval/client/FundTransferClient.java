package com.cib.approval.client;

import com.cib.approval.client.dto.UpdateTransferStatusRequest;
import com.cib.common.dto.ApiResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

/**
 * Feign client back into fund-transfer-service. Invoked after every checker
 * action (APPROVE/REJECT/SEND_BACK) to keep the transfer's status in sync -
 * fund-transfer-service remains the source of truth for TransferStatus and
 * validates the transition itself via its own TransferStatusValidator.
 */
@FeignClient(name = "fund-transfer-service", path = "/api/v1/transfers")
public interface FundTransferClient {

    @PatchMapping("/{id}/status")
    ApiResponse<Object> updateTransferStatus(@PathVariable("id") Long transferId,
                                              @RequestBody UpdateTransferStatusRequest request);
}
