package com.ai_support_ticket_triage.ai.dto.responce;

import com.ai_support_ticket_triage.ai.classification.TicketClassification;
import com.ai_support_ticket_triage.ai.enums.SupportTeam;
import com.ai_support_ticket_triage.ai.enums.TicketCategory;
import com.ai_support_ticket_triage.ai.enums.TicketPriority;
import com.ai_support_ticket_triage.ai.enums.TicketSentiment;

/**
 * API response for ticket classification results.
 *
 * <p>Returns the AI classification of a support ticket including
 * category, priority, team assignment, and sentiment analysis.</p>
 *
 * @param category the ticket category determined by AI
 * @param priority the priority level determined by AI
 * @param team the support team assigned by AI
 * @param sentiment the detected customer sentiment
 * @param reason the explanation for the classification
 */
public record TicketClassificationResponse(
        TicketCategory category,
        TicketPriority priority,
        SupportTeam team,
        TicketSentiment sentiment,
        String reason
) {

    /**
     * Converts a domain classification to an API response.
     *
     * @param classification the domain classification object
     * @return the API response
     */
    public static TicketClassificationResponse from(
            final TicketClassification classification
    ) {
        return new TicketClassificationResponse(
                classification.category(),
                classification.priority(),
                classification.team(),
                classification.sentiment(),
                classification.reason()
        );
    }
}