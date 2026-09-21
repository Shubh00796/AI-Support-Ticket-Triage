package com.ai_support_ticket_triage.ai.chunks;

import java.util.List;
import java.util.UUID;

/**
 * Represents a document chunk with its generated embedding vector.
 *
 * <p>Contains both the text content and its corresponding embedding
 * vector for semantic similarity search and analysis.</p>
 *
 * @param chunkId the unique identifier of the chunk entity
 * @param documentId the parent document identifier
 * @param chunkIndex the zero-based index within the document
 * @param pageNumber the page number the chunk originated from
 * @param text the chunk text content
 * @param vector the embedding vector representation of the text
 */
public record EmbeddedChunk(
        UUID chunkId,
        UUID documentId,
        int chunkIndex,
        int pageNumber,
        String text,
        List<Float> vector
) {
}