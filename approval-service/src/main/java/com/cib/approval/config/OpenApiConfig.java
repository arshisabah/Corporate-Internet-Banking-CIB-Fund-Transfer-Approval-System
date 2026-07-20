package com.cib.approval.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI approvalServiceOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Approval Service API")
                        .description("Handles Approve/Reject/Send-Back checker actions, approval history and audit trail")
                        .version("v1.0")
                        .contact(new Contact().name("CIB Platform Team")));
    }
}
