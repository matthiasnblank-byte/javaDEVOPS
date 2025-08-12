package com.example.devops.web;

import io.swagger.v3.oas.models.ExternalDocumentation;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {
    @Bean
    public OpenAPI todoOpenAPI() {
        return new OpenAPI()
                .info(new Info().title("Todo API").version("v1").description("OpenAPI Spezifikation für die Todo-API"))
                .externalDocs(new ExternalDocumentation().description("H2 Console").url("/h2-console"));
    }
}



