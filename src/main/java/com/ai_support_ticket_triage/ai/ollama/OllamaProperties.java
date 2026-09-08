package com.ai_support_ticket_triage.ai.ollama;


import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "app.ollama")
public record OllamaProperties(
        String baseUrl,
        String chatModel
) {
}