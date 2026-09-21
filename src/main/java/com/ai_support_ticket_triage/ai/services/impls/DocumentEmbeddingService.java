package com.ai_support_ticket_triage.ai.services.impls;

import com.ai_support_ticket_triage.ai.chunks.DocumentChunk;
import com.ai_support_ticket_triage.ai.chunks.EmbeddedChunk;
import com.ai_support_ticket_triage.ai.embeddings.Embedding;
import com.ai_support_ticket_triage.ai.embeddings.EmbeddingService;
import com.ai_support_ticket_triage.ai.entity.DocumentChunkEntity;
import com.ai_support_ticket_triage.ai.vectors.VectorStore;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Service for generating embeddings for document chunks.
 *
 * <p>Embeds document chunks using an embedding service and stores
 * the resulting vectors in a vector store for semantic search.</p>
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class DocumentEmbeddingService {

    private final EmbeddingService embeddingService;
    private final VectorStore vectorStore;

    /**
     * Generates embeddings for document chunks and stores them.
     *
     * @param chunks domain document chunks to embed
     * @param savedEntities persisted chunk entities
     * @return list of embedded chunks
     * @throws NullPointerException if chunks or savedEntities is null
     * @throws IllegalArgumentException if sizes don't match
     */
    public List<EmbeddedChunk> embedChunks(
            final List<DocumentChunk> chunks,
            final List<DocumentChunkEntity> savedEntities
    ) {
        validateInputs(chunks, savedEntities);
        log.debug("Starting embedding generation for {} chunks", chunks.size());

        final List<EmbeddedChunk> embeddedChunks = new ArrayList<>(chunks.size());

        for (int i = 0; i < chunks.size(); i++) {
            embeddedChunks.add(
                    embedAndSaveChunk(
                            chunks.get(i),
                            savedEntities.get(i),
                            i
                    )
            );
        }

        log.info("Embedded {} chunks successfully", embeddedChunks.size());
        return embeddedChunks;
    }

    /**
     * Validates that chunks and entities have matching non-null lists of equal size.
     *
     * @param chunks document chunks
     * @param savedEntities persisted chunk entities
     * @throws NullPointerException if either parameter is null
     * @throws IllegalArgumentException if sizes don't match
     */
    private void validateInputs(
            final List<DocumentChunk> chunks,
            final List<DocumentChunkEntity> savedEntities
    ) {
        Objects.requireNonNull(chunks, "chunks must not be null");
        Objects.requireNonNull(savedEntities, "savedEntities must not be null");

        if (chunks.size() != savedEntities.size()) {
            final String message = "Chunks and saved entities must have the same size: " +
                    "chunks=" + chunks.size() +
                    ", entities=" + savedEntities.size();
            log.error(message);
            throw new IllegalArgumentException(message);
        }
    }

    /**
     * Embeds a single chunk and stores it in the vector store.
     *
     * @param chunk domain document chunk
     * @param savedEntity persisted chunk entity
     * @param index position in the chunks list (for logging)
     * @return the embedded chunk
     * @throws NullPointerException if chunk, entity, or embedding is null
     */
    private EmbeddedChunk embedAndSaveChunk(
            final DocumentChunk chunk,
            final DocumentChunkEntity savedEntity,
            final int index
    ) {
        Objects.requireNonNull(
                chunk,
                "chunk at index " + index + " must not be null"
        );
        Objects.requireNonNull(
                savedEntity,
                "saved entity at index " + index + " must not be null"
        );

        log.debug("Embedding chunk at index: {}", index);
        final Embedding embedding = Objects.requireNonNull(
                embeddingService.embed(chunk.text()),
                "embedding must not be null for chunk at index " + index
        );

        final EmbeddedChunk embeddedChunk = new EmbeddedChunk(
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
                        "embedding vector must not be null for chunk at index " + index
                )
        );

        vectorStore.save(embeddedChunk);
        log.debug("Chunk embedded and stored at index: {}", index);

        return embeddedChunk;
    }
}