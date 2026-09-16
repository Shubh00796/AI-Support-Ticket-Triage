package com.ai_support_ticket_triage.ai.classification;

import com.ai_support_ticket_triage.ai.enums.SupportTeam;
import com.ai_support_ticket_triage.ai.enums.TicketCategory;
import com.ai_support_ticket_triage.ai.enums.TicketPriority;
import com.ai_support_ticket_triage.ai.enums.TicketSentiment;

public record TicketClassification(
        TicketCategory category,
        TicketPriority priority,
        SupportTeam team,
        TicketSentiment sentiment,
        String reason
) {

    public TicketClassification {

        if (category == null) {
            throw new IllegalArgumentException("Category must not be null");
        }
        if (priority == null) {
            throw new IllegalArgumentException("Priority must not be null");
        }
        if (team == null) {
            throw new IllegalArgumentException("Support team must not be null");
        }
        if (sentiment == null) {
            throw new IllegalArgumentException("Sentiment must not be null");
        }

        if (reason == null || reason.isBlank()) {
            throw new IllegalArgumentException(
                    "Classification reason must not be blank"
            );
        }

        if (reason.length() > 500) {
            throw new IllegalArgumentException(
                    "Classification reason must not exceed 500 characters"
            );
        }
    }
}