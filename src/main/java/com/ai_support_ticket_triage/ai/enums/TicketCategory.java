package com.ai_support_ticket_triage.ai.enums;

/**
 * Enumeration of support ticket categories.
 *
 * <p>Used to categorize tickets for routing and prioritization.</p>
 */
public enum TicketCategory {
    /** Billing-related issues */
    PAYMENT,
    /** Login and account access issues */
    AUTHENTICATION,
    /** Technical problems and bugs */
    TECHNICAL,
    /** Account settings and profile issues */
    ACCOUNT,
    /** General inquiries and other issues */
    GENERAL
}