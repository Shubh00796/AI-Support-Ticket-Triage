package com.ai_support_ticket_triage.ai.parser;

import com.ai_support_ticket_triage.ai.exceptions.UnsupportedDocumentTypeException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Objects;

/**
 * Resolves the appropriate document parser based on content type.
 *
 * <p>Uses a strategy pattern to find the correct parser for different
 * document formats. Parsers are registered as Spring beans and auto-wired
 * into this resolver.</p>
 */
@Component
@Slf4j
public class DocumentParserResolver {

    private final List<DocumentParser> parsers;

    /**
     * Creates a resolver with the available document parsers.
     *
     * @param parsers list of available document parser implementations
     */
    public DocumentParserResolver(
            final List<DocumentParser> parsers
    ) {
        this.parsers = List.copyOf(Objects.requireNonNull(parsers, "parsers must not be null"));
        log.info("DocumentParserResolver initialized with {} parsers", this.parsers.size());
    }

    /**
     * Finds and returns a parser that supports the given content type.
     *
     * @param detectedContentType the document content type
     * @return a parser that supports the content type
     * @throws UnsupportedDocumentTypeException if no parser supports the type or type is blank
     * @throws NullPointerException if contentType is null
     */
    public DocumentParser resolve(
            final String detectedContentType
    ) {
        Objects.requireNonNull(detectedContentType, "detectedContentType must not be null");

        if (detectedContentType.isBlank()) {
            log.error("Document content type is blank");
            throw new UnsupportedDocumentTypeException(
                    "Document content type must not be blank"
            );
        }

        if (parsers.isEmpty()) {
            log.error("No document parsers are available");
            throw new UnsupportedDocumentTypeException(
                    "No document parsers are available"
            );
        }

        log.debug("Resolving parser for content type: {}", detectedContentType);

        return parsers.stream()
                .filter(parser -> parser.supports(detectedContentType))
                .peek(parser -> log.debug("Found parser for content type: {}", detectedContentType))
                .findFirst()
                .orElseThrow(() -> {
                    log.error("Unsupported document type: {}", detectedContentType);
                    return new UnsupportedDocumentTypeException(
                            "Unsupported document type: " + detectedContentType
                    );
                });
    }
}
