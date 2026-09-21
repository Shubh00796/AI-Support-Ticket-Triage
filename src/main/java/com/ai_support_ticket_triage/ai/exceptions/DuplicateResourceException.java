package com.ai_support_ticket_triage.ai.exceptions;

/**
 * Exception thrown when attempting to create a resource that already exists.
 *
 * <p>This indicates a conflict where a duplicate resource was detected
 * during a create operation. Should result in a 409 Conflict HTTP response.</p>
 */
public class DuplicateResourceException extends RuntimeException {

    /**
     * Creates a new exception with a message.
     *
     * @param message description of the duplicate resource
     */
    public DuplicateResourceException(final String message) {
        super(message);
    }
}
