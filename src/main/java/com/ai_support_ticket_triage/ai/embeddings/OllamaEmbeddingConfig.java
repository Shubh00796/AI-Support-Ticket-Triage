package com.ai_support_ticket_triage.ai.embeddings;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestClient;

@Configuration
public class OllamaEmbeddingConfig {

    @Bean
    RestClient ollamaEmbeddingRestClient(OllamaEmbeddingProperties properties) {
        return RestClient.builder()
                .baseUrl(properties.baseUrl())
                .build();
    }
}
