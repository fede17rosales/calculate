package com.example.rosales.challenge.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

        @Bean
        public OpenAPI apiInfo() {
            return new OpenAPI()
                    .info(new Info()
                            .title("Challenge API")
                            .description("API de cálculo con porcentaje dinámico")
                            .version("1.0"));
        }
    }
