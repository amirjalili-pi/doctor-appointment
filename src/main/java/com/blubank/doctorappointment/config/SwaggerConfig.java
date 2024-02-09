package com.blubank.doctorappointment.config;

import io.swagger.v3.oas.models.ExternalDocumentation;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {
    @Bean
    public OpenAPI springShopOpenAPI() {
        return new OpenAPI()
                .info(new Info().title("Appointment API")
                        .description("this is a sample for reserve, take, delete doctorAppointment")
                        .version("v0.0.1")
                        .license(new License().name("BluBank").url("https://blubank.sb24.ir")))
                .externalDocs(new ExternalDocumentation());
//                        .description("SpringShop Wiki Documentation"));
//                        .url("https://springshop.wiki.github.org/docs"));
    }
}
