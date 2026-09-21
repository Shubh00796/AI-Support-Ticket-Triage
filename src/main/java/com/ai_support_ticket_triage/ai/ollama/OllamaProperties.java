package com.ai_support_ticket_triage.ai.ollama;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * Configuration properties for Ollama integration.
 *
 * <p>Binds to properties prefixed with "app.ollama" from the application
 * configuration files.</p>
 *
 * @param baseUrl the base URL of the Ollama API server
 * @param chatModel the model name to use for chat completions
 */
@ConfigurationProperties(prefix = "app.ollama")
public record OllamaProperties(
        String baseUrl,
        String chatModel
) {
}