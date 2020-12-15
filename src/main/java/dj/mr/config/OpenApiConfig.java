package dj.mr.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Configuration;

/**
 * Swagger document Configuration.
 */
@Configuration
public class OpenApiConfig {

    /**
     * This method creates Swagger document for this application.
     *
     * @return OpenAPI
     */
    public OpenAPI customAPI() {
        return new OpenAPI()
                .components(new Components())
                .info(new Info()
                        .title("Financial Data Application API")
                        .description("This is REST API for Financial "
                                + "Data Application"));
    }

}
