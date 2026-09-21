package com.ai_support_ticket_triage.ai.exceptions;

/**
 * Exception thrown when embedding generation or storage fails.
 *
 * <p>This indicates a failure in the embedding service, which could be due to
 * communication issues with the embedding provider or storage failures.</p>
 */
public class EmbeddingServiceException extends RuntimeException {

    /**
     * Creates a new exception with a message.
     *
     * @param message description of what went wrong
     */
    public EmbeddingServiceException(final String message) {
        super(message);
    }

    /**
     * Creates a new exception with a message and root cause.
     *
     * @param message description of what went wrong
     * @param cause the root cause exception
     */
    public EmbeddingServiceException(final String message, final Throwable cause) {
        super(message, cause);
    }
}
