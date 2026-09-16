package com.ai_support_ticket_triage.ai.classification;

import com.ai_support_ticket_triage.ai.exceptions.TicketClassificationParseException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import tools.jackson.databind.json.JsonMapper;

@Component
@RequiredArgsConstructor
public class TicketClassificationResponseParser {

    private final JsonMapper jsonMapper;

    public TicketClassification parse(String response) {
        if (response == null || response.isBlank()) {
            throw new TicketClassificationParseException(
                    "Failed to parse ticket classification response",
                    new IllegalArgumentException("Classification response must not be blank")
            );
        }

        try {
            return jsonMapper.readValue(
                    response,
                    TicketClassification.class
            );
        } catch (Exception e) {
            throw new TicketClassificationParseException(
                    "Failed to parse ticket classification response",
                    e
            );
        }
    }
}
