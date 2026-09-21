package com.ai_support_ticket_triage.ai.enums;

/**
 * Enumeration of support teams available for ticket routing.
 *
 * <p>Each team specializes in specific ticket categories and issues.</p>
 */
public enum SupportTeam {
    /** Handles billing and payment-related issues */
    BILLING_SUPPORT,
    /** Handles technical problems and bugs */
    TECHNICAL_SUPPORT,
    /** Handles account settings and access issues */
    ACCOUNT_SUPPORT,
    /** Handles general inquiries and other issues */
    GENERAL_SUPPORT
}