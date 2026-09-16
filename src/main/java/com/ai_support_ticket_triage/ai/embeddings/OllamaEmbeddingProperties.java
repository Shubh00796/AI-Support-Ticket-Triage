package com.ai_support_ticket_triage.ai.embeddings;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

import jakarta.validation.constraints.NotBlank;

@Validated
@ConfigurationProperties(prefix = "app.embedding.ollama")
public record OllamaEmbeddingProperties(
        @NotBlank String baseUrl,
        @NotBlank String model
) {
}
