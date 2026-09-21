package com.ai_support_ticket_triage.ai.exceptions;

/**
 * Exception thrown when an uploaded document type is not supported.
 *
 * <p>This indicates that the document's content type (MIME type) is not
 * supported by any available document parser. Should result in a 415
 * Unsupported Media Type HTTP response.</p>
 */
public class UnsupportedDocumentTypeException extends RuntimeException {

    /**
     * Creates a new exception with a message.
     *
     * @param message description of the unsupported document type
     */
    public UnsupportedDocumentTypeException(final String message) {
        super(message);
    }
}
