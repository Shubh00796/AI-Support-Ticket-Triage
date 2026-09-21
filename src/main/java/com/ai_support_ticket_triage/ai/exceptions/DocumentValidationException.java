package com.ai_support_ticket_triage.ai.exceptions;

/**
 * Exception thrown when document validation fails.
 *
 * <p>This indicates that an uploaded document did not meet validation
 * requirements such as file size, format, or content constraints.</p>
 */
public class DocumentValidationException extends RuntimeException {

    /**
     * Creates a new validation exception with a message.
     *
     * @param message description of the validation failure
     */
    public DocumentValidationException(final String message) {
        super(message);
    }

    /**
     * Creates a new validation exception with a message and root cause.
     *
     * @param message description of the validation failure
     * @param cause the root cause exception
     */
    public DocumentValidationException(final String message, final Throwable cause) {
        super(message, cause);
    }
}
