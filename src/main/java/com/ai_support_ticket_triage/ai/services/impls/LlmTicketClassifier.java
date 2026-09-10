package com.ai_support_ticket_triage.ai.services.impls;


import com.ai_support_ticket_triage.ai.classification.TicketClassification;
import com.ai_support_ticket_triage.ai.classification.TicketClassificationPromptBuilder;
import com.ai_support_ticket_triage.ai.classification.TicketClassificationResponseParser;
import com.ai_support_ticket_triage.ai.classification.TicketClassificationValidator;
import com.ai_support_ticket_triage.ai.entity.Ticket;
import com.ai_support_ticket_triage.ai.ollama.OllamaClient;
import com.ai_support_ticket_triage.ai.services.AiClassifier;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class LlmTicketClassifier implements AiClassifier {

    private final TicketClassificationPromptBuilder promptBuilder;
    private final OllamaClient ollamaClient;
    private final TicketClassificationResponseParser responseParser;
    private final TicketClassificationValidator validator;

    @Override
    public TicketClassification classify(Ticket ticket) {

        log.debug("Classifying ticket. Ticket ID: {}", ticket.getId());

        String prompt = promptBuilder.build(ticket);

        String response = ollamaClient.chat(prompt);

        TicketClassification classification =
                responseParser.parse(response);

        validator.validate(classification);

        log.debug(
                "Ticket classified successfully. Ticket ID: {}, Category: {}, Priority: {}",
                ticket.getId(),
                classification.category(),
                classification.priority()
        );

        return classification;
    }
}