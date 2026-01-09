package com.pavan.ecommerce.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI ecommerceOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("E-Commerce Microservices Platform API")
                        .description(
                                "Enterprise-grade e-commerce REST API with Spring Boot 3, Elasticsearch, and Kafka")
                        .version("1.0.0")
                        .contact(new Contact()
                                .name("Pavan M")
                                .url("https://github.com/pavanm101"))
                        .license(new License()
                                .name("MIT License")
                                .url("https://opensource.org/licenses/MIT")));
    }
}
