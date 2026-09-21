package com.ai_support_ticket_triage.ai.exceptions;

import java.util.UUID;

/**
 * Exception thrown when a ticket cannot be found by ID.
 *
 * <p>This should be caught by the global exception handler to return
 * a 404 response to the client.</p>
 */
public class TicketNotFoundException extends RuntimeException {

    /**
     * Creates a new exception for a missing ticket.
     *
     * @param ticketId the ID of the ticket that was not found
     */
    public TicketNotFoundException(final UUID ticketId) {
        super("Ticket not found with id: " + ticketId);
    }
}
