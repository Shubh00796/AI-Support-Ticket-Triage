package com.ai_support_ticket_triage.ai.chunks;

import java.util.UUID;

/**
 * Represents a chunk (segment) of a document.
 *
 * <p>A document is split into chunks for embedding generation and
 * semantic search. Chunks preserve document structure through page
 * and chunk indices.</p>
 *
 * @param documentId the parent document identifier
 * @param chunkIndex the zero-based index of this chunk within the document
 * @param pageNumber the page number this chunk originated from
 * @param text the chunk text content
 */
public record DocumentChunk(
        UUID documentId,
        int chunkIndex,
        int pageNumber,
        String text
) {
}
