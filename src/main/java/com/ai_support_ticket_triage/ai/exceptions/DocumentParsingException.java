package com.ai_support_ticket_triage.ai.exceptions;

public class DocumentParsingException extends RuntimeException {
    public DocumentParsingException(String message) {
        super(message);
    }

    public DocumentParsingException(String message, Throwable cause) {
        super(message, cause);
    }
}
