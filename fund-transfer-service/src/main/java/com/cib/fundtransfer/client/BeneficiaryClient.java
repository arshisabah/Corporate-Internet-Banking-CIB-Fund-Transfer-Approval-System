package com.cib.fundtransfer.client;

import com.cib.common.dto.ApiResponse;
import com.cib.fundtransfer.client.dto.BeneficiaryValidationResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

/**
 * Feign client for beneficiary-service. The 'name' attribute matches
 * spring.application.name of beneficiary-service as registered in Eureka -
 * no hardcoded host:port, resolved via client-side load balancing.
 */
@FeignClient(name = "beneficiary-service", path = "/api/v1/beneficiaries")
public interface BeneficiaryClient {

    @GetMapping("/{id}/validate")
    ApiResponse<BeneficiaryValidationResponse> validateBeneficiary(@PathVariable("id") Long beneficiaryId);
}
