package com.ai_support_ticket_triage.ai.classification;

import com.ai_support_ticket_triage.ai.enums.SupportTeam;
import com.ai_support_ticket_triage.ai.enums.TicketCategory;
import com.ai_support_ticket_triage.ai.enums.TicketPriority;
import com.ai_support_ticket_triage.ai.enums.TicketSentiment;

import java.util.Objects;

public record TicketClassification(
        TicketCategory category,
        TicketPriority priority,
        SupportTeam team,
        TicketSentiment sentiment,
        String reason
) {

    public TicketClassification {

        Objects.requireNonNull(category, "Category must not be null");
        Objects.requireNonNull(priority, "Priority must not be null");
        Objects.requireNonNull(team, "Support team must not be null");
        Objects.requireNonNull(sentiment, "Sentiment must not be null");

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