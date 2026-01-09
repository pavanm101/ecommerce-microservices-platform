package com.pavan.ecommerce;

import com.pavan.ecommerce.storage.StorageProperties;
import com.pavan.ecommerce.storage.StorageService;
import com.pavan.ecommerce.validators.GroupValidator;
import com.pavan.ecommerce.validators.OrderValidator;
import com.pavan.ecommerce.validators.ProductValidator;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.validation.Validator;

@SpringBootApplication
@EnableConfigurationProperties(StorageProperties.class)
public class EcommerceStarterApplication {

    public static void main(String[] args) {
        SpringApplication.run(EcommerceStarterApplication.class, args);
    }

    @Bean
    CommandLineRunner init(StorageService storageService) {
        return (args) -> {
            storageService.init();
        };
    }

    @Bean
    public Validator productValidator() {
        return new ProductValidator();
    }

    @Bean
    public Validator groupValidator() {
        return new GroupValidator();
    }

    @Bean
    public Validator orderValidator() {
        return new OrderValidator();
    }
}
