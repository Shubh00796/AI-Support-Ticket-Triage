package com.ai_support_ticket_triage.ai.exceptions;

/**
 * Exception thrown when document parsing fails.
 *
 * <p>This indicates that the document content could not be parsed
 * successfully into pages/text. The root cause should be available
 * via the underlying exception.</p>
 */
public class DocumentParsingException extends RuntimeException {

    /**
     * Creates a new parsing exception with a message.
     *
     * @param message description of what went wrong
     */
    public DocumentParsingException(final String message) {
        super(message);
    }

    /**
     * Creates a new parsing exception with a message and root cause.
     *
     * @param message description of what went wrong
     * @param cause the root cause exception
     */
    public DocumentParsingException(final String message, final Throwable cause) {
        super(message, cause);
    }
}
