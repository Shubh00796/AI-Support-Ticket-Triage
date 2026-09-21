package com.ai_support_ticket_triage.ai.parser;

import java.util.List;

/**
 * Interface for parsing documents of specific content types into pages.
 *
 * <p>Implementing classes should handle parsing of a specific document format
 * and extract text content organized by pages.</p>
 */
public interface DocumentParser {

    /**
     * Checks if this parser can handle the given content type.
     *
     * @param contentType the MIME type of the document
     * @return true if this parser supports the content type, false otherwise
     */
    boolean supports(String contentType);

    /**
     * Parses document content into pages.
     *
     * @param content the raw document bytes
     * @return list of parsed pages with text content
     * @throws com.ai_support_ticket_triage.ai.exceptions.DocumentParsingException
     *         if parsing fails
     */
    List<ParsedPage> parse(byte[] content);
}