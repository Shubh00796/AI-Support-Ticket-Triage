package com.ai_support_ticket_triage.ai.exceptions;

/**
 * Thrown when a requested resource cannot be found.
 *
 */
public class ResourceNotFoundException extends RuntimeException {

    /**
     * Creates a new exception with the specified message.
     *
     * @param message exception message
     */
    public ResourceNotFoundException(String message) {
        super(message);
    }
}