package com.ai_support_ticket_triage.ai.enums;

/**
 * Enumeration of ticket priority levels.
 *
 * <p>Used to determine response time and resource allocation.</p>
 */
public enum TicketPriority {
    /** Non-urgent issues with low impact */
    LOW,
    /** Standard priority issues */
    MEDIUM,
    /** Urgent issues requiring prompt attention */
    HIGH,
    /** Severe issues requiring immediate attention */
    CRITICAL
}