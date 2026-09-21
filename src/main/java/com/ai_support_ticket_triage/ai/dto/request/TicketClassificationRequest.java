package com.ai_support_ticket_triage.ai.dto.request;


/**
 * Request payload containing the ticket message to classify.
 *
 * @param message the customer support ticket message
 */
public record TicketClassificationRequest(
        String message
) {
}