package com.probemore.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    public OpenAPI customAPI() {
        return new OpenAPI()
                .components(new Components())
                .info(new Info()
                          .title("Financial Data Application API")
                          .description("This is REST API for Financial Data Application"));
    }

}
