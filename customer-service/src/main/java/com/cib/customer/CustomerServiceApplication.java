package com.cib.customer;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

/**
 * Entry point for the Customer Service - manages corporate customers
 * (companies) and corporate users (MAKER/CHECKER/ADMIN) for the CIB platform.
 * Registers with Eureka via @EnableDiscoveryClient and pulls externalized
 * config from the Config Server on startup.
 */
@SpringBootApplication(scanBasePackages = {"com.cib.customer", "com.cib.common"})
@EnableDiscoveryClient
public class CustomerServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(CustomerServiceApplication.class, args);
    }
}
