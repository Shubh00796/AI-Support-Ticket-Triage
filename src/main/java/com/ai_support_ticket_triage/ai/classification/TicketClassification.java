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
}