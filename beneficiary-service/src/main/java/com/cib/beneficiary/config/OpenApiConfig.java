package com.cib.beneficiary.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI beneficiaryServiceOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Beneficiary Service API")
                        .description("Manages beneficiary CRUD and ACTIVE-status validation consumed by fund-transfer-service")
                        .version("v1.0")
                        .contact(new Contact().name("CIB Platform Team")));
    }
}
