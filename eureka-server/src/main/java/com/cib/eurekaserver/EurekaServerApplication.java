package com.cib.eurekaserver;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;

/**
 * Entry point for the Eureka Service Registry. Every microservice in the
 * CIB Fund Transfer & Approval System (customer, beneficiary, fund-transfer,
 * approval, api-gateway) registers here on startup and discovers each other
 * by application name rather than hardcoded host:port - enabling Feign +
 * client-side load balancing via 'lb://' URIs.
 */
@SpringBootApplication
@EnableEurekaServer
public class EurekaServerApplication {

    public static void main(String[] args) {
        SpringApplication.run(EurekaServerApplication.class, args);
    }
}
