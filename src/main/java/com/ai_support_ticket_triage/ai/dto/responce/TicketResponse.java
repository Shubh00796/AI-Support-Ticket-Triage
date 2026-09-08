package com.ai_support_ticket_triage.ai.dto.responce;




import com.ai_support_ticket_triage.ai.enums.TicketStatus;

import java.time.LocalDateTime;
import java.util.UUID;

public record TicketResponse(
        UUID id,
        String message,
        TicketStatus status,
        LocalDateTime createdAt
) {
}