package com.cib.fundtransfer.config;

import feign.Logger;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Feign-specific configuration. BASIC logging level surfaces request method,
 * URL, response status and execution time in logs - useful for tracing
 * cross-service calls to beneficiary-service and approval-service without
 * the verbosity of FULL (headers/body) in production.
 */
@Configuration
public class FeignClientConfig {

    @Bean
    public Logger.Level feignLoggerLevel() {
        return Logger.Level.BASIC;
    }
}
