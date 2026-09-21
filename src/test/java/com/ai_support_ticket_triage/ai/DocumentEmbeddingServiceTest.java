package com.ai_support_ticket_triage.ai;

import com.ai_support_ticket_triage.ai.chunks.DocumentChunk;
import com.ai_support_ticket_triage.ai.chunks.EmbeddedChunk;
import com.ai_support_ticket_triage.ai.embeddings.Embedding;
import com.ai_support_ticket_triage.ai.embeddings.EmbeddingService;
import com.ai_support_ticket_triage.ai.entity.DocumentChunkEntity;
import com.ai_support_ticket_triage.ai.services.impls.DocumentEmbeddingService;
import com.ai_support_ticket_triage.ai.vectors.VectorStore;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class DocumentEmbeddingServiceTest {

    private final EmbeddingService embeddingService =
            mock(EmbeddingService.class);

    private final VectorStore vectorStore =
            mock(VectorStore.class);

    private final DocumentEmbeddingService documentEmbeddingService =
            new DocumentEmbeddingService(
                    embeddingService,
                    vectorStore
            );

    @Test
    void shouldEmbedAndSaveAllChunks() {
        UUID documentId = UUID.randomUUID();
        UUID firstChunkId = UUID.randomUUID();
        UUID secondChunkId = UUID.randomUUID();

        DocumentChunk firstChunk =
                new DocumentChunk(documentId, 0, 1, "First chunk");
        DocumentChunk secondChunk =
                new DocumentChunk(documentId, 1, 2, "Second chunk");

        DocumentChunkEntity firstSavedEntity = mock(DocumentChunkEntity.class);
        DocumentChunkEntity secondSavedEntity = mock(DocumentChunkEntity.class);

        when(firstSavedEntity.getId()).thenReturn(firstChunkId);
        when(secondSavedEntity.getId()).thenReturn(secondChunkId);

        when(embeddingService.embed("First chunk"))
                .thenReturn(new Embedding(List.of(0.1f, 0.2f)));
        when(embeddingService.embed("Second chunk"))
                .thenReturn(new Embedding(List.of(0.3f, 0.4f)));

        List<EmbeddedChunk> embeddedChunks =
                documentEmbeddingService.embedChunks(
                        List.of(firstChunk, secondChunk),
                        List.of(firstSavedEntity, secondSavedEntity)
                );

        assertEquals(2, embeddedChunks.size());
        assertEquals(
                new EmbeddedChunk(
                        firstChunkId,
                        documentId,
                        0,
                        1,
                        "First chunk",
                        List.of(0.1f, 0.2f)
                ),
                embeddedChunks.get(0)
        );
        assertEquals(
                new EmbeddedChunk(
                        secondChunkId,
                        documentId,
                        1,
                        2,
                        "Second chunk",
                        List.of(0.3f, 0.4f)
                ),
                embeddedChunks.get(1)
        );

        verify(vectorStore).save(embeddedChunks.get(0));
        verify(vectorStore).save(embeddedChunks.get(1));
    }

    @Test
    void shouldRejectMismatchedCollectionSizes() {
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> documentEmbeddingService.embedChunks(
                        List.of(new DocumentChunk(
                                UUID.randomUUID(),
                                0,
                                1,
                                "chunk"
                        )),
                        List.of()
                )
        );

        // Verify error message contains key information about the size mismatch
        String message = exception.getMessage();
        assertEquals(true,
                message.contains("Chunks and saved entities must have the same size") &&
                message.contains("chunks=1") &&
                message.contains("entities=0")
        );
    }

    @Test
    void shouldRejectNullChunkEntry() {
        DocumentChunkEntity savedEntity = mock(DocumentChunkEntity.class);
        List<DocumentChunk> chunks = new ArrayList<>();

        chunks.add(null);

        when(savedEntity.getId()).thenReturn(UUID.randomUUID());

        NullPointerException exception = assertThrows(
                NullPointerException.class,
                () -> documentEmbeddingService.embedChunks(
                        chunks,
                        List.of(savedEntity)
                )
        );

        assertEquals(
                "chunk at index 0 must not be null",
                exception.getMessage()
        );
    }

    @Test
    void shouldRejectSavedEntityWithoutId() {
        DocumentChunk chunk =
                new DocumentChunk(UUID.randomUUID(), 0, 1, "chunk");
        DocumentChunkEntity savedEntity = mock(DocumentChunkEntity.class);

        when(savedEntity.getId()).thenReturn(null);
        when(embeddingService.embed("chunk"))
                .thenReturn(new Embedding(List.of(0.1f)));

        NullPointerException exception = assertThrows(
                NullPointerException.class,
                () -> documentEmbeddingService.embedChunks(
                        List.of(chunk),
                        List.of(savedEntity)
                )
        );

        assertEquals(
                "saved entity id at index 0 must not be null",
                exception.getMessage()
        );
    }
}

