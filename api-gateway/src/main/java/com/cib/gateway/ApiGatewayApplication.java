package com.cib.gateway;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

/**
 * Entry point for the API Gateway - the single entry point for all external
 * traffic into the CIB platform. Routes to customer-service, beneficiary-
 * service, fund-transfer-service and approval-service via Eureka-resolved
 * 'lb://' URIs (routes defined centrally in config-repo/api-gateway.yml).
 * Runs on the reactive WebFlux/Netty stack, not servlet/Tomcat.
 */
@SpringBootApplication
@EnableDiscoveryClient
public class ApiGatewayApplication {

    public static void main(String[] args) {
        SpringApplication.run(ApiGatewayApplication.class, args);
    }
}
