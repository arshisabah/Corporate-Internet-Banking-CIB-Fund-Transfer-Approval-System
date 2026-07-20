package com.cib.beneficiary;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

/**
 * Entry point for the Beneficiary Service - manages beneficiary CRUD and
 * exposes the ACTIVE-status validation endpoint consumed by
 * fund-transfer-service via Feign before any transfer is created.
 */
@SpringBootApplication(scanBasePackages = {"com.cib.beneficiary", "com.cib.common"})
@EnableDiscoveryClient
public class BeneficiaryServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(BeneficiaryServiceApplication.class, args);
    }
}
