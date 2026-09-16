package com.ai_support_ticket_triage.ai.embeddings;

import com.ai_support_ticket_triage.ai.exceptions.EmbeddingServiceException;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClientException;

import java.util.List;

@Component
public class OllamaEmbeddingClient {

    private static final String EMBED_PATH = "/api/embed";

    private final org.springframework.web.client.RestClient restClient;
    private final OllamaEmbeddingProperties properties;

    public OllamaEmbeddingClient(
            @Qualifier("ollamaEmbeddingRestClient") org.springframework.web.client.RestClient restClient,
            OllamaEmbeddingProperties properties
    ) {
        this.restClient = restClient;
        this.properties = properties;
    }

    public List<Float> embed(String text) {
        try {
            EmbeddingResponse response = restClient
                    .post()
                    .uri(EMBED_PATH)
                    .body(new EmbeddingRequest(properties.model(), text))
                    .retrieve()
                    .body(EmbeddingResponse.class);

            return extractVector(response);
        } catch (RestClientException exception) {
            throw new EmbeddingServiceException(
                    "Failed to generate embedding",
                    exception
            );
        }
    }

    private List<Float> extractVector(EmbeddingResponse response) {
        if (response == null
                || response.embeddings() == null
                || response.embeddings().isEmpty()
                || response.embeddings().get(0) == null
                || response.embeddings().get(0).isEmpty()) {
            throw new EmbeddingServiceException("Ollama returned an empty embedding");
        }

        return response.embeddings().get(0);
    }

    private record EmbeddingRequest(
            String model,
            String input
    ) {
    }

    private record EmbeddingResponse(
            List<List<Float>> embeddings
    ) {
    }
}
