package com.cib.customer.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI customerServiceOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Customer Service API")
                        .description("Manages corporate customers and corporate users (MAKER/CHECKER/ADMIN) for the CIB platform")
                        .version("v1.0")
                        .contact(new Contact().name("CIB Platform Team")));
    }
}
