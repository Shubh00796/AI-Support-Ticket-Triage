package com.ai_support_ticket_triage.ai.embeddings;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class OllamaEmbeddingService implements EmbeddingService {

    private final OllamaEmbeddingClient embeddingClient;

    @Override
    public Embedding embed(String text) {
        if (text == null || text.isBlank()) {
            throw new IllegalArgumentException("Text to embed must not be blank");
        }

        return new Embedding(embeddingClient.embed(text));
    }
}
