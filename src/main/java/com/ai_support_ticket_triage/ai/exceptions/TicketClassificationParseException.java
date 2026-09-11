package com.ai_support_ticket_triage.ai.exceptions;

public class TicketClassificationParseException extends RuntimeException {
    public TicketClassificationParseException(String message) {
        super(message);
    }

    public TicketClassificationParseException(String message, Throwable cause) {
        super(message, cause);
    }
}
