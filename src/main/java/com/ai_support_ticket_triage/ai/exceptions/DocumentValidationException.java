package com.ai_support_ticket_triage.ai.exceptions;

public class DocumentValidationException extends RuntimeException {
    public DocumentValidationException(String message) {
        super(message);
    }

    public DocumentValidationException(String message, Throwable cause) {
        super(message, cause);
    }
}
