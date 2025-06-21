package com.ventasWeb.cl.ventasWeb.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


// Configura la docuemtación.
@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
            .info(new Info()
                .title("API ventasWeb.v1 2025")
                .version("1.0")
                .description("Documentación de la API para realizar compras por internet."));
    }
}
