package com.cib.common.config;

import com.cib.common.exception.GlobalExceptionHandler;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

/**
 * Auto-configuration entry point for cib-common. Registered via
 * META-INF/spring/org.springframework.boot.autoconfigure.AutoConfiguration.imports
 * so every microservice that depends on cib-common automatically gets:
 *   - GlobalExceptionHandler (standard error responses)
 *   - AuditorAwareImpl (populates createdBy/updatedBy)
 *   - JPA Auditing enabled (populates createdAt/updatedAt)
 *
 * No manual @Import or @ComponentScan changes needed in downstream services.
 */
@Configuration
@EnableJpaAuditing(auditorAwareRef = "auditorAware")
public class CibCommonAutoConfiguration {

    @Bean
    @ConditionalOnMissingBean
    public GlobalExceptionHandler globalExceptionHandler() {
        return new GlobalExceptionHandler();
    }

    @Bean("auditorAware")
    @ConditionalOnMissingBean(name = "auditorAware")
    public AuditorAware<String> auditorAware() {
        return new AuditorAwareImpl();
    }
}
