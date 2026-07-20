package com.cib.fundtransfer.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI fundTransferServiceOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Fund Transfer Service API")
                        .description("Initiates transfers, validates beneficiaries via Feign, submits to approval-service, tracks status and history")
                        .version("v1.0")
                        .contact(new Contact().name("CIB Platform Team")));
    }
}
