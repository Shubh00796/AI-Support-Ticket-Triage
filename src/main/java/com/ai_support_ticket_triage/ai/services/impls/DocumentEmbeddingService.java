package com.ai_support_ticket_triage.ai.services.impls;

import com.ai_support_ticket_triage.ai.chunks.DocumentChunk;
import com.ai_support_ticket_triage.ai.chunks.EmbeddedChunk;
import com.ai_support_ticket_triage.ai.embeddings.Embedding;
import com.ai_support_ticket_triage.ai.embeddings.EmbeddingService;
import com.ai_support_ticket_triage.ai.entity.DocumentChunkEntity;
import com.ai_support_ticket_triage.ai.vectors.VectorStore;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class DocumentEmbeddingService {

    private final EmbeddingService embeddingService;
    private final VectorStore vectorStore;

    public List<EmbeddedChunk> embedChunks(
            List<DocumentChunk> chunks,
            List<DocumentChunkEntity> savedEntities
    ) {
        validateInputs(chunks, savedEntities);

        List<EmbeddedChunk> embeddedChunks =
                new ArrayList<>(chunks.size());

        for (int i = 0; i < chunks.size(); i++) {
            embeddedChunks.add(
                    embedAndSaveChunk(
                            chunks.get(i),
                            savedEntities.get(i),
                            i
                    )
            );
        }

        return embeddedChunks;
    }

    private void validateInputs(
            List<DocumentChunk> chunks,
            List<DocumentChunkEntity> savedEntities
    ) {
        Objects.requireNonNull(chunks, "chunks must not be null");
        Objects.requireNonNull(savedEntities, "savedEntities must not be null");

        if (chunks.size() != savedEntities.size()) {
            throw new IllegalArgumentException(
                    "Chunks and saved entities must have the same size"
            );
        }
    }

    private EmbeddedChunk embedAndSaveChunk(
            DocumentChunk chunk,
            DocumentChunkEntity savedEntity,
            int index
    ) {
        Objects.requireNonNull(
                chunk,
                "chunk at index " + index + " must not be null"
        );
        Objects.requireNonNull(
                savedEntity,
                "saved entity at index " + index + " must not be null"
        );

        Embedding embedding = Objects.requireNonNull(
                embeddingService.embed(chunk.text()),
                "embedding must not be null"
        );

        EmbeddedChunk embeddedChunk =
                new EmbeddedChunk(
                        Objects.requireNonNull(
                                savedEntity.getId(),
                                "saved entity id at index " + index + " must not be null"
                        ),
                        chunk.documentId(),
                        chunk.chunkIndex(),
                        chunk.pageNumber(),
                        chunk.text(),
                        Objects.requireNonNull(
                                embedding.vector(),
                                "embedding vector must not be null"
                        )
                );

        vectorStore.save(embeddedChunk);

        return embeddedChunk;
    }
}