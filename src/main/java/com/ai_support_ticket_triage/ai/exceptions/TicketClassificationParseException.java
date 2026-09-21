package com.ai_support_ticket_triage.ai.exceptions;

/**
 * Exception thrown when ticket classification response parsing fails.
 *
 * <p>This indicates that the LLM response could not be parsed into
 * a valid ticket classification structure.</p>
 */
public class TicketClassificationParseException extends RuntimeException {

    /**
     * Creates a new parsing exception with a message.
     *
     * @param message description of what went wrong
     */
    public TicketClassificationParseException(final String message) {
        super(message);
    }

    /**
     * Creates a new parsing exception with a message and root cause.
     *
     * @param message description of what went wrong
     * @param cause the root cause exception
     */
    public TicketClassificationParseException(final String message, final Throwable cause) {
        super(message, cause);
    }
}
