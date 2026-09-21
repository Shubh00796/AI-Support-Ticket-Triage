package com.ai_support_ticket_triage.ai.services.impls;

import com.ai_support_ticket_triage.ai.chunks.DocumentChunk;
import com.ai_support_ticket_triage.ai.chunks.DocumentChunker;
import com.ai_support_ticket_triage.ai.parser.ParsedPage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;
import java.util.stream.Stream;

/**
 * Chunker implementation that splits documents semantically.
 *
 * <p>Chunks are created by preserving paragraph boundaries when possible,
 * respecting a maximum chunk length. Pages are chunked independently
 * to preserve document structure information.</p>
 *
 * <p>If a single paragraph exceeds the maximum length, it is split
 * into multiple chunks to maintain reasonable size constraints.</p>
 */
@Component
@Slf4j
public class SemanticDocumentChunker implements DocumentChunker {

    private static final int MAX_CHUNK_LENGTH = 3000;
    private static final String PARAGRAPH_SEPARATOR = "\n\n";

    /**
     * Chunks document pages while preserving semantic boundaries.
     *
     * @param documentId the document identifier
     * @param pages parsed document pages with content
     * @return list of document chunks derived from pages
     * @throws NullPointerException if documentId or pages is null
     */
    @Override
    public List<DocumentChunk> chunk(
            final UUID documentId,
            final List<ParsedPage> pages
    ) {
        Objects.requireNonNull(documentId, "documentId must not be null");
        Objects.requireNonNull(pages, "pages must not be null");
        log.debug("Starting document chunking. Document ID: {}, Pages: {}", documentId, pages.size());

        final List<DocumentChunk> chunks = new ArrayList<>();

        for (final ParsedPage page : pages) {
            if (page == null || page.text() == null || page.text().isBlank()) {
                continue;
            }

            chunkPage(chunks, documentId, page);
        }

        log.info("Document chunking completed. Document ID: {}, Total chunks: {}", documentId, chunks.size());
        return chunks;
    }

    /**
     * Chunks a single page by splitting on paragraph boundaries.
     *
     * @param chunks list to accumulate chunks
     * @param documentId the document identifier
     * @param page the page to chunk
     */
    private void chunkPage(
            final List<DocumentChunk> chunks,
            final UUID documentId,
            final ParsedPage page
    ) {
        final StringBuilder currentChunk = new StringBuilder();

        for (final String paragraph : splitIntoParagraphs(page.text())) {
            if (paragraph.length() > MAX_CHUNK_LENGTH) {
                flushCurrentChunk(chunks, documentId, page.pageNumber(), currentChunk);
                addLargeParagraphChunks(chunks, documentId, page.pageNumber(), paragraph);
                continue;
            }

            if (willExceedMaxLength(currentChunk, paragraph)) {
                flushCurrentChunk(chunks, documentId, page.pageNumber(), currentChunk);
            }

            appendParagraph(currentChunk, paragraph);
        }

        flushCurrentChunk(chunks, documentId, page.pageNumber(), currentChunk);
    }

    /**
     * Checks if adding a paragraph would exceed maximum chunk length.
     *
     * @param currentChunk the current accumulated chunk text
     * @param paragraph the paragraph to potentially add
     * @return true if adding the paragraph would exceed max length
     */
    private boolean willExceedMaxLength(
            final StringBuilder currentChunk,
            final String paragraph
    ) {
        final int separatorLength = currentChunk.length() == 0 ? 0 : PARAGRAPH_SEPARATOR.length();
        return currentChunk.length() + separatorLength + paragraph.length() > MAX_CHUNK_LENGTH;
    }

    /**
     * Appends a paragraph to the current chunk with proper separator.
     *
     * @param currentChunk the accumulated chunk text
     * @param paragraph the paragraph to append
     */
    private void appendParagraph(
            final StringBuilder currentChunk,
            final String paragraph
    ) {
        if (currentChunk.length() > 0) {
            currentChunk.append(PARAGRAPH_SEPARATOR);
        }
        currentChunk.append(paragraph);
    }

    /**
     * Finalizes a chunk by adding it to the chunks list if non-empty.
     *
     * @param chunks list to accumulate chunks
     * @param documentId the document identifier
     * @param pageNumber the page number for metadata
     * @param currentChunk the accumulated chunk text
     */
    private void flushCurrentChunk(
            final List<DocumentChunk> chunks,
            final UUID documentId,
            final int pageNumber,
            final StringBuilder currentChunk
    ) {
        if (currentChunk.length() == 0) {
            return;
        }

        chunks.add(
                new DocumentChunk(
                        documentId,
                        chunks.size(),
                        pageNumber,
                        currentChunk.toString()
                )
        );

        currentChunk.setLength(0);
    }

    /**
     * Splits a large paragraph into multiple chunks.
     *
     * @param chunks list to accumulate chunks
     * @param documentId the document identifier
     * @param pageNumber the page number for metadata
     * @param paragraph the paragraph to split
     */
    private void addLargeParagraphChunks(
            final List<DocumentChunk> chunks,
            final UUID documentId,
            final int pageNumber,
            final String paragraph
    ) {
        for (int start = 0; start < paragraph.length(); start += MAX_CHUNK_LENGTH) {
            final int end = Math.min(start + MAX_CHUNK_LENGTH, paragraph.length());

            chunks.add(
                    new DocumentChunk(
                            documentId,
                            chunks.size(),
                            pageNumber,
                            paragraph.substring(start, end)
                    )
            );
        }
    }

    /**
     * Splits text into paragraphs using blank line separation.
     *
     * @param text the text to split
     * @return list of non-blank paragraphs
     */
    private List<String> splitIntoParagraphs(final String text) {
        if (text == null || text.isBlank()) {
            return List.of();
        }

        return Stream.of(text.trim().split("\\R\\s*\\R"))
                .map(String::trim)
                .filter(paragraph -> !paragraph.isBlank())
                .toList();
    }
}
