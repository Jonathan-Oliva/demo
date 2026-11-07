package com.example.demo.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Metadata general que aparece en Swagger UI (título, descripción, versión).
 * La documentación de cada endpoint puntual va con @Operation en el
 * controller correspondiente.
 */
@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI demoOpenAPI() {
        return new OpenAPI()
                .components(new Components().addSecuritySchemes("bearerAuth",
                        new SecurityScheme()
                                .type(SecurityScheme.Type.HTTP)
                                .scheme("bearer")
                                .bearerFormat("JWT")))
                .info(new Info()
                        .title("TP3 · API de productos, favoritos y listas")
                        .description("API REST con registro, autenticación JWT, autorización por propietario, "
                                + "persistencia PostgreSQL y catálogo de productos externo. Los endpoints de "
                                + "favoritos y listas requieren el esquema Bearer JWT.")
                        .version("v1"));
    }
}
