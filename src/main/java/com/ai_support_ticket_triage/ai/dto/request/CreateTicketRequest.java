package com.ai_support_ticket_triage.ai.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record CreateTicketRequest(

        @NotBlank(message = "Ticket message must not be blank")
        @Size(
                max = 5000,
                message = "Ticket message must not exceed 5000 characters"
        )
        String message

) {
}