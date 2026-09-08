package com.ai_support_ticket_triage.ai.exceptions;

import java.util.UUID;

public class TicketNotFoundException extends RuntimeException {
    public TicketNotFoundException(UUID message) {
        super(String.valueOf(message));
    }
}
