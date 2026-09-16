package com.ai_support_ticket_triage.ai.embeddings;

/**
 * Produces vector embeddings for input text.
 */
public interface EmbeddingService {

    /**
     * Converts the given text into an embedding vector.
     *
     * @param text input text
     * @return embedding vector
     */
    Embedding embed(String text);
}
