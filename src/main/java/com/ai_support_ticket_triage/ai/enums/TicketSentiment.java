package com.ai_support_ticket_triage.ai.enums;

/**
 * Enumeration of customer sentiment detected in tickets.
 *
 * <p>Used to analyze customer emotion and adjust support approach accordingly.</p>
 */
public enum TicketSentiment {
    /** Customer is satisfied or pleased */
    POSITIVE,
    /** Customer is neutral in tone */
    NEUTRAL,
    /** Customer is frustrated but not hostile */
    FRUSTRATED,
    /** Customer is angry or hostile */
    ANGRY
}