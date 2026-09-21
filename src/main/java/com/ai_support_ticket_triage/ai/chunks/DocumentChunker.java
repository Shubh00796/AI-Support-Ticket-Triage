package com.ai_support_ticket_triage.ai.chunks;





import com.ai_support_ticket_triage.ai.parser.ParsedPage;

import java.util.List;
import java.util.UUID;

/**
 * Interface for splitting documents into chunks for embedding and search.
 *
 * <p>Implementations should provide semantic-aware chunking strategies
 * that preserve document structure and context.</p>
 */
public interface DocumentChunker {

    /**
     * Chunks parsed document pages into smaller segments.
     *
     * @param documentId the parent document identifier
     * @param pages the parsed pages to chunk
     * @return list of document chunks
     */
    List<DocumentChunk> chunk(
            UUID documentId,
            List<ParsedPage> pages
    );
}