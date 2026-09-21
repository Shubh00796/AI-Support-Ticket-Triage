package com.ai_support_ticket_triage.ai.enums;

/**
 * Enumeration of possible ticket statuses throughout its lifecycle.
 */
public enum TicketStatus {
    /** Ticket is open and waiting for response */
    OPEN,
    /** Ticket is currently being handled */
    IN_PROGRESS,
    /** Ticket has been resolved */
    RESOLVED
}