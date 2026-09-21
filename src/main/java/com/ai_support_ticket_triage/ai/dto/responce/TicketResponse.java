package com.ai_support_ticket_triage.ai.dto.responce;

import com.ai_support_ticket_triage.ai.enums.SupportTeam;
import com.ai_support_ticket_triage.ai.enums.TicketCategory;
import com.ai_support_ticket_triage.ai.enums.TicketPriority;
import com.ai_support_ticket_triage.ai.enums.TicketSentiment;
import com.ai_support_ticket_triage.ai.enums.TicketStatus;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 * API response representing a support ticket.
 *
 * <p>Contains all ticket information including message, status, metadata,
 * and ai-generated classification attributes.</p>
 *
 * @param id the unique ticket identifier
 * @param message the ticket message content
 * @param status the current ticket status
 * @param createdAt the timestamp when the ticket was created
 * @param category the ticket category from AI classification
 * @param priority the priority level from AI classification
 * @param team the assigned support team from AI classification
 * @param sentiment the detected customer sentiment from AI classification
 * @param classificationReason the explanation for the classification
 */
public record TicketResponse(
        UUID id,
        String message,
        TicketStatus status,
        LocalDateTime createdAt,
        TicketCategory category,
        TicketPriority priority,
        SupportTeam team,
        TicketSentiment sentiment,
        String classificationReason
) {
}