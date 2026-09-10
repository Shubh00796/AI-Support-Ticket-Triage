package com.ai_support_ticket_triage.ai;


import com.ai_support_ticket_triage.ai.classification.TicketClassification;
import com.ai_support_ticket_triage.ai.classification.TicketClassificationResponseParser;
import com.ai_support_ticket_triage.ai.enums.SupportTeam;
import com.ai_support_ticket_triage.ai.enums.TicketCategory;
import com.ai_support_ticket_triage.ai.enums.TicketPriority;
import com.ai_support_ticket_triage.ai.enums.TicketSentiment;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import tools.jackson.databind.json.JsonMapper;

import static org.junit.jupiter.api.Assertions.assertEquals;

class TicketClassificationResponseParserTest {

    private TicketClassificationResponseParser parser;

    @BeforeEach
    void setUp() {
        parser = new TicketClassificationResponseParser(
                JsonMapper.builder().build()
        );
    }

    @Test
    void shouldParseValidClassificationJson() {

        String response = """
                {
                  "category": "PAYMENT",
                  "priority": "HIGH",
                  "team": "BILLING",
                  "sentiment": "FRUSTRATED",
                  "reason": "Payment was deducted but order is still pending."
                }
                """;

        TicketClassification result = parser.parse(response);

        assertEquals(TicketCategory.PAYMENT, result.category());
        assertEquals(TicketPriority.HIGH, result.priority());
        assertEquals(SupportTeam.BILLING, result.team());
        assertEquals(TicketSentiment.FRUSTRATED, result.sentiment());
        assertEquals(
                "Payment was deducted but order is still pending.",
                result.reason()
        );
    }
}