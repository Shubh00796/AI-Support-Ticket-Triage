package com.ai_support_ticket_triage.ai.ollama;


import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

import java.util.List;

@Component
public class OllamaClient {

    private final RestClient restClient;
    private final String chatModel;

    public OllamaClient(
            RestClient restClient,
            OllamaProperties properties
    ) {
        this.restClient = restClient;
        this.chatModel = properties.chatModel();
    }

    public String chat(String prompt) {

        OllamaChatRequest request = new OllamaChatRequest(
                chatModel,
                List.of(
                        new OllamaMessage(
                                "user",
                                prompt
                        )
                ),
                false
        );

        OllamaChatResponse response = restClient
                .post()
                .uri("/api/chat")
                .body(request)
                .retrieve()
                .body(OllamaChatResponse.class);

        return response.message().content();
    }
}