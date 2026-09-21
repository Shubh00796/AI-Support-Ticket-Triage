package com.ai_support_ticket_triage.ai.validations;

import com.ai_support_ticket_triage.ai.classification.TicketClassification;
import org.springframework.stereotype.Component;

/**
 * Validator for ticket classification results.
 *
 * <p>Validates that all required classification fields are populated
 * and contain valid values. Used as a defensive check after parsing
 * LLM responses.</p>
 */
@Component
public class TicketClassificationValidator {

    /**
     * Validates a complete ticket classification.
     *
     * <p>Ensures all required fields are non-null and reason is non-blank.</p>
     *
     * @param classification the classification to validate
     * @throws IllegalArgumentException if any required field is null or blank
     */
    public void validate(final TicketClassification classification) {
        requireNonNull(classification, "Ticket classification");

        requireNonNull(classification.category(), "Ticket classification category");
        requireNonNull(classification.priority(), "Ticket classification priority");
        requireNonNull(classification.team(), "Ticket classification team");
        requireNonNull(classification.sentiment(), "Ticket classification sentiment");
        requireNotBlank(classification.reason(), "Ticket classification reason");
    }

    /**
     * Checks that a value is not null.
     *
     * @param value the value to check
     * @param fieldName the field name for error messages
     * @throws IllegalArgumentException if value is null
     */
    private void requireNonNull(final Object value, final String fieldName) {
        if (value == null) {
            throw new IllegalArgumentException(fieldName + " must not be null");
        }
    }

    /**
     * Checks that a string is not null or blank.
     *
     * @param value the value to check
     * @param fieldName the field name for error messages
     * @throws IllegalArgumentException if value is null or blank
     */
    private void requireNotBlank(final String value, final String fieldName) {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException(fieldName + " must not be blank");
        }
    }
}