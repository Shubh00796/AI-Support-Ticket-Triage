package com.ai_support_ticket_triage.ai.services.impls;

import com.ai_support_ticket_triage.ai.chunks.DocumentChunk;
import com.ai_support_ticket_triage.ai.chunks.EmbeddedChunk;
import com.ai_support_ticket_triage.ai.embeddings.Embedding;
import com.ai_support_ticket_triage.ai.embeddings.EmbeddingService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class DocumentEmbeddingService {

    private final EmbeddingService embeddingService;

    public List<EmbeddedChunk> embedChunks(List<DocumentChunk> chunks) {
        Objects.requireNonNull(chunks, "chunks must not be null");

        return chunks.stream()
                .filter(Objects::nonNull)
                .map(this::embedChunk)
                .toList();
    }

    private EmbeddedChunk embedChunk(DocumentChunk chunk) {

        Objects.requireNonNull(
                chunk,
                "chunk must not be null"
        );
        Embedding embedding = embeddingService.embed(chunk.text());

        return new EmbeddedChunk(
                null,
                chunk.documentId(),
                chunk.chunkIndex(),
                chunk.pageNumber(),
                chunk.text(),
                embedding.vector()
        );
    }
}
