package com.ai_support_ticket_triage.ai.validations;


import com.ai_support_ticket_triage.ai.classification.TicketClassification;
import org.springframework.stereotype.Component;

@Component
public class TicketClassificationValidator {

    public void validate(TicketClassification classification) {
        requireNonNull(classification, "Ticket classification");

        requireNonNull(classification.category(), "Ticket classification category");
        requireNonNull(classification.priority(), "Ticket classification priority");
        requireNonNull(classification.team(), "Ticket classification team");
        requireNonNull(classification.sentiment(), "Ticket classification sentiment");
        requireNotBlank(classification.reason(), "Ticket classification reason");
    }

    private void requireNonNull(Object value, String fieldName) {
        if (value == null) {
            throw new IllegalArgumentException(fieldName + " must not be null");
        }
    }

    private void requireNotBlank(String value, String fieldName) {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException(fieldName + " must not be blank");
        }
    }
}