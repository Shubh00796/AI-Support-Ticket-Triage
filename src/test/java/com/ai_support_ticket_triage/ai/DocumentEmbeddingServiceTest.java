package com.ai_support_ticket_triage.ai;


import com.ai_support_ticket_triage.ai.chunks.DocumentChunk;
import com.ai_support_ticket_triage.ai.chunks.EmbeddedChunk;
import com.ai_support_ticket_triage.ai.embeddings.Embedding;
import com.ai_support_ticket_triage.ai.embeddings.EmbeddingService;
import com.ai_support_ticket_triage.ai.services.impls.DocumentEmbeddingService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class DocumentEmbeddingServiceTest {

    @Mock
    private EmbeddingService embeddingService;

    @InjectMocks
    private DocumentEmbeddingService documentEmbeddingService;

    @Test
    void shouldGenerateEmbeddingForDocumentChunks() {

        UUID documentId = UUID.randomUUID();

        DocumentChunk chunk =
                new DocumentChunk(
                        documentId,
                        0,
                        1,
                        "Payment was deducted but order is pending."
                );

        Embedding embedding =
                new Embedding(
                        Arrays.asList(
                                0.1f,
                                0.2f,
                                0.3f
                        )
                );

        when(embeddingService.embed(chunk.text()))
                .thenReturn(embedding);

        List<EmbeddedChunk> result =
                documentEmbeddingService.embedChunks(
                        Arrays.asList(chunk)
                );

        assertEquals(1, result.size());

        EmbeddedChunk embeddedChunk =
                result.get(0);

        assertEquals(
                documentId,
                embeddedChunk.documentId()
        );

        assertEquals(
                0,
                embeddedChunk.chunkIndex()
        );

        assertEquals(
                1,
                embeddedChunk.pageNumber()
        );

        assertEquals(
                "Payment was deducted but order is pending.",
                embeddedChunk.text()
        );

        assertEquals(
                Arrays.asList(0.1f, 0.2f, 0.3f),
                embeddedChunk.vector()
        );

        verify(embeddingService)
                .embed(chunk.text());
    }
}