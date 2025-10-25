package com.example.eventsync.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI eventFeedbackOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Event Feedback Analyzer API")
                        .version("1.0")
                        .description("API for analyzing event feedback sentiment")
                        .contact(new Contact()
                                .name("Migle Kirilovaite")
                                .url("https://github.com/migle-kirilovaite")));
    }
}