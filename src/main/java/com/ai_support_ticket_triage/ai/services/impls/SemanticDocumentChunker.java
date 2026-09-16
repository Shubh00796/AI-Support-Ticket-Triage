package com.ai_support_ticket_triage.ai.services.impls;

import com.ai_support_ticket_triage.ai.chunks.DocumentChunk;
import com.ai_support_ticket_triage.ai.chunks.DocumentChunker;
import com.ai_support_ticket_triage.ai.parser.ParsedPage;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;
import java.util.stream.Stream;

@Component
public class SemanticDocumentChunker implements DocumentChunker {

    private static final int MAX_CHUNK_LENGTH = 3000;
    private static final String PARAGRAPH_SEPARATOR = "\n\n";

    @Override
    public List<DocumentChunk> chunk(
            UUID documentId,
            List<ParsedPage> pages
    ) {
        Objects.requireNonNull(documentId, "documentId must not be null");
        Objects.requireNonNull(pages, "pages must not be null");

        List<DocumentChunk> chunks = new ArrayList<>();

        for (ParsedPage page : pages) {
            if (page == null || page.text() == null || page.text().isBlank()) {
                continue;
            }

            StringBuilder currentChunk = new StringBuilder();

            for (String paragraph : splitIntoParagraphs(page.text())) {
                if (paragraph.length() > MAX_CHUNK_LENGTH) {
                    flushCurrentChunk(
                            chunks,
                            documentId,
                            page.pageNumber(),
                            currentChunk
                    );
                    addLargeParagraphChunks(
                            chunks,
                            documentId,
                            page.pageNumber(),
                            paragraph
                    );
                    continue;
                }

                if (willExceedMaxLength(currentChunk, paragraph)) {
                    flushCurrentChunk(
                            chunks,
                            documentId,
                            page.pageNumber(),
                            currentChunk
                    );
                }

                appendParagraph(currentChunk, paragraph);
            }

            flushCurrentChunk(
                    chunks,
                    documentId,
                    page.pageNumber(),
                    currentChunk
            );
        }

        return chunks;
    }

    private boolean willExceedMaxLength(
            StringBuilder currentChunk,
            String paragraph
    ) {
        int separatorLength = currentChunk.length() == 0
                ? 0
                : PARAGRAPH_SEPARATOR.length();

        return currentChunk.length() + separatorLength + paragraph.length() > MAX_CHUNK_LENGTH;
    }

    private void appendParagraph(
            StringBuilder currentChunk,
            String paragraph
    ) {
        if (currentChunk.length() > 0) {
            currentChunk.append(PARAGRAPH_SEPARATOR);
        }

        currentChunk.append(paragraph);
    }

    private void flushCurrentChunk(
            List<DocumentChunk> chunks,
            UUID documentId,
            int pageNumber,
            StringBuilder currentChunk
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

    private void addLargeParagraphChunks(
            List<DocumentChunk> chunks,
            UUID documentId,
            int pageNumber,
            String paragraph
    ) {
        for (int start = 0; start < paragraph.length(); start += MAX_CHUNK_LENGTH) {
            int end = Math.min(start + MAX_CHUNK_LENGTH, paragraph.length());

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

    private List<String> splitIntoParagraphs(
            String text
    ) {
        if (text == null || text.isBlank()) {
            return List.of();
        }

        return Stream.of(text.trim().split("\\R\\s*\\R"))
                .map(String::trim)
                .filter(paragraph -> !paragraph.isBlank())
                .toList();
    }
}
