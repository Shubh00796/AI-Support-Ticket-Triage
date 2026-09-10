package com.ai_support_ticket_triage.ai.ollama;


import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestClient;

@Configuration
public class OllamaConfig {

    @Bean
    RestClient ollamaRestClient(OllamaProperties properties) {

        validateProps(properties);

        return RestClient.builder()
                .baseUrl(properties.baseUrl())
                .build();
    }

    private static void validateProps(OllamaProperties properties) {
        if (properties.baseUrl() == null || properties.baseUrl().isBlank()) {
            throw new IllegalStateException(
                    "app.ollama.base-url is not configured"
            );
        }

        if (properties.chatModel() == null || properties.chatModel().isBlank()) {
            throw new IllegalStateException(
                    "app.ollama.chat-model is not configured"
            );
        }
    }
}