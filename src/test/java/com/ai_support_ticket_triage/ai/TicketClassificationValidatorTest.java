package com.ai_support_ticket_triage.ai;


import com.ai_support_ticket_triage.ai.classification.TicketClassification;
import com.ai_support_ticket_triage.ai.validations.TicketClassificationValidator;
import com.ai_support_ticket_triage.ai.enums.SupportTeam;
import com.ai_support_ticket_triage.ai.enums.TicketCategory;
import com.ai_support_ticket_triage.ai.enums.TicketPriority;
import com.ai_support_ticket_triage.ai.enums.TicketSentiment;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

class TicketClassificationValidatorTest {

    private final TicketClassificationValidator validator =
            new TicketClassificationValidator();

    @Test
    void shouldAcceptValidClassification() {
        assertDoesNotThrow(() ->
                validator.validate(validClassification())
        );
    }

    @Test
    void shouldRejectNullClassification() {
        assertThrows(
                IllegalArgumentException.class,
                () -> validator.validate(null)
        );
    }

    @Test
    void shouldRejectNullCategory() {
        assertThrows(
                IllegalArgumentException.class,
                () -> validator.validate(
                        classification(null, TicketPriority.MEDIUM)
                )
        );
    }

    @Test
    void shouldRejectNullPriority() {
        assertThrows(
                IllegalArgumentException.class,
                () -> validator.validate(
                        classification(TicketCategory.TECHNICAL, null)
                )
        );
    }

    @Test
    void shouldRejectNullTeam() {
        assertThrows(
                IllegalArgumentException.class,
                () -> validator.validate(
                        new TicketClassification(
                                TicketCategory.TECHNICAL,
                                TicketPriority.MEDIUM,
                                null,
                                TicketSentiment.FRUSTRATED,
                                "Application is not working."
                        )
                )
        );
    }

    @Test
    void shouldRejectNullSentiment() {
        assertThrows(
                IllegalArgumentException.class,
                () -> validator.validate(
                        new TicketClassification(
                                TicketCategory.TECHNICAL,
                                TicketPriority.MEDIUM,
                                SupportTeam.TECHNICAL_SUPPORT,
                                null,
                                "Application is not working."
                        )
                )
        );
    }

    @Test
    void shouldRejectNullReason() {
        assertThrows(
                IllegalArgumentException.class,
                () -> validator.validate(
                        new TicketClassification(
                                TicketCategory.TECHNICAL,
                                TicketPriority.MEDIUM,
                                SupportTeam.TECHNICAL_SUPPORT,
                                TicketSentiment.FRUSTRATED,
                                null
                        )
                )
        );
    }

    @Test
    void shouldRejectBlankReason() {
        assertThrows(
                IllegalArgumentException.class,
                () -> validator.validate(
                        new TicketClassification(
                                TicketCategory.TECHNICAL,
                                TicketPriority.MEDIUM,
                                SupportTeam.TECHNICAL_SUPPORT,
                                TicketSentiment.FRUSTRATED,
                                "   "
                        )
                )
        );
    }

    @Test
    void shouldRejectEmptyReason() {
        assertThrows(
                IllegalArgumentException.class,
                () -> validator.validate(
                        new TicketClassification(
                                TicketCategory.TECHNICAL,
                                TicketPriority.MEDIUM,
                                SupportTeam.TECHNICAL_SUPPORT,
                                TicketSentiment.FRUSTRATED,
                                ""
                        )
                ));
    }

    private TicketClassification validClassification() {
        return new TicketClassification(
                TicketCategory.TECHNICAL,
                TicketPriority.MEDIUM,
                SupportTeam.TECHNICAL_SUPPORT,
                TicketSentiment.FRUSTRATED,
                "Application is not working."
        );
    }

    private TicketClassification classification(
            TicketCategory category,
            TicketPriority priority
    ) {
        return new TicketClassification(
                category,
                priority,
                SupportTeam.TECHNICAL_SUPPORT,
                TicketSentiment.FRUSTRATED,
                "Application is not working."
        );
    }
}