package com.cib.approval;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

/**
 * Entry point for the Approval Service - the final stage of the CIB
 * workflow. Receives approval requests from fund-transfer-service, exposes
 * Approve/Reject/Send-Back actions for checkers, maintains approval history
 * and a compliance audit trail, and calls back into fund-transfer-service
 * to keep TransferStatus in sync.
 */
@SpringBootApplication(scanBasePackages = {"com.cib.approval", "com.cib.common"})
@EnableDiscoveryClient
@EnableFeignClients(basePackages = "com.cib.approval.client")
public class ApprovalServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(ApprovalServiceApplication.class, args);
    }
}
