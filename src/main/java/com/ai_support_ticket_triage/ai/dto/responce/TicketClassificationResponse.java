package com.ai_support_ticket_triage.ai.dto.responce;

import com.ai_support_ticket_triage.ai.classification.TicketClassification;
import com.ai_support_ticket_triage.ai.enums.SupportTeam;
import com.ai_support_ticket_triage.ai.enums.TicketCategory;
import com.ai_support_ticket_triage.ai.enums.TicketPriority;
import com.ai_support_ticket_triage.ai.enums.TicketSentiment;

public record TicketClassificationResponse(
        TicketCategory category,
        TicketPriority priority,
        SupportTeam team,
        TicketSentiment sentiment,
        String reason
) {

    public static TicketClassificationResponse from(
            TicketClassification classification
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