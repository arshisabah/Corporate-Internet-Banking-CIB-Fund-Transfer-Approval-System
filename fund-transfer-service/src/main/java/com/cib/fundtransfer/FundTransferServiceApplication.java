package com.cib.fundtransfer;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

/**
 * Entry point for the Fund Transfer Service - the orchestration hub of the
 * CIB platform. Initiates transfers, validates beneficiaries via Feign call
 * to beneficiary-service, submits to approval-service for checker action,
 * and tracks full status/audit history.
 */
@SpringBootApplication(scanBasePackages = {"com.cib.fundtransfer", "com.cib.common"})
@EnableDiscoveryClient
@EnableFeignClients(basePackages = "com.cib.fundtransfer.client")
public class FundTransferServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(FundTransferServiceApplication.class, args);
    }
}
