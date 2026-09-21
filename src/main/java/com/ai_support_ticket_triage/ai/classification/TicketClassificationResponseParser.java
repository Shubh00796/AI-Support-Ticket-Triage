package com.ai_support_ticket_triage.ai.classification;

import com.ai_support_ticket_triage.ai.exceptions.TicketClassificationParseException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import tools.jackson.databind.json.JsonMapper;

import java.util.Objects;

/**
 * Parser for deserializing LLM responses into ticket classifications.
 *
 * <p>Handles JSON deserialization from LLM responses and validates
 * the resulting classification objects. Throws custom exceptions
 * for parsing failures with detailed error messages.</p>
 */
@Component
@RequiredArgsConstructor
@Slf4j
public class TicketClassificationResponseParser {

    private final JsonMapper jsonMapper;

    /**
     * Parses a JSON response string into a ticket classification.
     *
     * @param response the JSON response from the LLM
     * @return the deserialized ticket classification
     * @throws TicketClassificationParseException if response is blank or cannot be parsed
     */
    public TicketClassification parse(final String response) {
        Objects.requireNonNull(response, "response must not be null");

        if (response.isBlank()) {
            log.error("Classification response is blank");
            throw new TicketClassificationParseException(
                    "Failed to parse ticket classification response: response is blank"
            );
        }

        try {
            log.debug("Parsing classification response");
            final TicketClassification classification = jsonMapper.readValue(
                    response,
                    TicketClassification.class
            );
            log.debug("Classification response parsed successfully");
            return classification;
        } catch (final Exception e) {
            log.error("Failed to parse classification response: {}", response, e);
            throw new TicketClassificationParseException(
                    "Failed to parse ticket classification response: invalid JSON format",
                    e
            );
        }
    }
}
