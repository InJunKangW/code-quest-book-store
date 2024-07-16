package com.nhnacademy.bookstoreinjun.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        Info info = new Info()
                .title("Product API")
                .version("1.0")
                .description("Product API");
        return new OpenAPI()
                .components(new Components())
                .info(info);
    }

}
