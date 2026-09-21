package com.ai_support_ticket_triage.ai.parser;

/**
 * Represents a single page of parsed document content.
 *
 * <p>Immutable value record containing the page number and extracted text content.
 * Page numbers are 1-based for human readability.</p>
 *
 * @param pageNumber the page number (1-based)
 * @param text the extracted text content from the page
 */
public record ParsedPage(
        int pageNumber,
        String text
) {
}