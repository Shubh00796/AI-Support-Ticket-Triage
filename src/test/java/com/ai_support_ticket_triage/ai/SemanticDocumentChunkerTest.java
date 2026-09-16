package com.ai_support_ticket_triage.ai;

import com.ai_support_ticket_triage.ai.chunks.DocumentChunk;
import com.ai_support_ticket_triage.ai.parser.ParsedPage;
import com.ai_support_ticket_triage.ai.services.impls.SemanticDocumentChunker;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class SemanticDocumentChunkerTest {

    private final SemanticDocumentChunker chunker =
            new SemanticDocumentChunker();

    private final UUID documentId = UUID.randomUUID();

    @Test
    void shouldCreateSingleChunkForSmallParagraph() {

        List<ParsedPage> pages = List.of(
                new ParsedPage(
                        1,
                        "Payment failed while placing the order."
                )
        );

        List<DocumentChunk> chunks =
                chunker.chunk(documentId, pages);

        assertEquals(1, chunks.size());

        DocumentChunk chunk = chunks.get(0);

        assertEquals(documentId, chunk.documentId());
        assertEquals(0, chunk.chunkIndex());
        assertEquals(1, chunk.pageNumber());
        assertEquals(
                "Payment failed while placing the order.",
                chunk.text()
        );
    }

    @Test
    void shouldCreateMultipleChunksWhenContentIsLarge() {

        String paragraph1 = "A".repeat(1500);
        String paragraph2 = "B".repeat(1500);

        List<ParsedPage> pages = List.of(
                new ParsedPage(
                        1,
                        paragraph1 + "\n\n" + paragraph2
                )
        );

        List<DocumentChunk> chunks =
                chunker.chunk(documentId, pages);

        assertEquals(2, chunks.size());

        assertEquals(0, chunks.get(0).chunkIndex());
        assertEquals(1, chunks.get(1).chunkIndex());

        assertEquals(1, chunks.get(0).pageNumber());
        assertEquals(1, chunks.get(1).pageNumber());
    }

    @Test
    void shouldSplitLargeParagraph() {

        String largeParagraph = "A".repeat(6000);

        List<ParsedPage> pages = List.of(
                new ParsedPage(1, largeParagraph)
        );

        List<DocumentChunk> chunks =
                chunker.chunk(documentId, pages);

        assertEquals(2, chunks.size());

        assertTrue(chunks.get(0).text().length() <= 3000);
        assertTrue(chunks.get(1).text().length() <= 3000);
    }

    @Test
    void shouldPreservePageNumbers() {

        List<ParsedPage> pages = List.of(
                new ParsedPage(
                        1,
                        "Information about payments."
                ),
                new ParsedPage(
                        2,
                        "Information about authentication."
                )
        );

        List<DocumentChunk> chunks =
                chunker.chunk(documentId, pages);

        assertEquals(2, chunks.size());

        assertEquals(1, chunks.get(0).pageNumber());
        assertEquals(2, chunks.get(1).pageNumber());
    }

    @Test
    void shouldNotCreateChunksForEmptyPage() {

        List<ParsedPage> pages = List.of(
                new ParsedPage(1, ""),
                new ParsedPage(2, "   ")
        );

        List<DocumentChunk> chunks =
                chunker.chunk(documentId, pages);

        assertTrue(chunks.isEmpty());
    }

    @Test
    void shouldKeepChunkIndexesSequential() {

        List<ParsedPage> pages = List.of(
                new ParsedPage(1, "First page."),
                new ParsedPage(2, "Second page."),
                new ParsedPage(3, "Third page.")
        );

        List<DocumentChunk> chunks =
                chunker.chunk(documentId, pages);

        for (int i = 0; i < chunks.size(); i++) {
            assertEquals(
                    i,
                    chunks.get(i).chunkIndex()
            );
        }
    }
}