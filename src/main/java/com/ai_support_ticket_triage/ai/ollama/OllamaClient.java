package com.ai_support_ticket_triage.ai.ollama;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

import java.util.List;
import java.util.Objects;

/**
 * Client for communicating with the Ollama API.
 *
 * <p>Sends chat requests to the configured Ollama model and returns
 * the generated responses. Handles the serialization and deserialization
 * of request and response objects.</p>
 */
@Component
@Slf4j
public class OllamaClient {

    private final RestClient restClient;
    private final String chatModel;

    /**
     * Creates a new Ollama client with the configured settings.
     *
     * @param restClient the REST client for making HTTP requests
     * @param properties Ollama configuration properties
     */
    public OllamaClient(
            @Qualifier("ollamaRestClient") final RestClient restClient,
            final OllamaProperties properties
    ) {
        this.restClient = Objects.requireNonNull(restClient, "restClient must not be null");
        this.chatModel = Objects.requireNonNull(properties, "properties must not be null").chatModel();
        log.info("OllamaClient initialized with model: {}", this.chatModel);
    }

    /**
     * Sends a prompt to the Ollama model and gets a response.
     *
     * @param prompt the prompt to send
     * @return the model's response text
     * @throws NullPointerException if prompt is null
     */
    public String chat(final String prompt) {
        Objects.requireNonNull(prompt, "prompt must not be null");
        log.debug("Sending prompt to Ollama model");

        final OllamaChatRequest request = new OllamaChatRequest(
                chatModel,
                List.of(
                        new OllamaMessage("user", prompt)
                ),
                false
        );

        final OllamaChatResponse response = restClient
                .post()
                .uri("/api/chat")
                .body(request)
                .retrieve()
                .body(OllamaChatResponse.class);

        final String responseText = response != null && response.message() != null
                ? response.message().content()
                : "";

        log.debug("Received response from Ollama model");
        return responseText;
    }
}
