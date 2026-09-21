package com.ai_support_ticket_triage.ai.controller;


import com.ai_support_ticket_triage.ai.classification.RagTicketClassifier;
import com.ai_support_ticket_triage.ai.classification.TicketClassification;
import com.ai_support_ticket_triage.ai.dto.request.TicketClassificationRequest;
import com.ai_support_ticket_triage.ai.dto.responce.TicketClassificationResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

/**
 * Exposes endpoints for classifying tickets with knowledge base context.
 */
@RestController
@RequestMapping("/api/tickets")
@RequiredArgsConstructor
public class RagClassificationController {

    private final RagTicketClassifier ragTicketClassifier;

    /**
     * Classifies a ticket request using the RAG classification flow.
     *
     * @param request the request containing the ticket message
     * @return the ticket classification response
     */
    @PostMapping("/classify-rag")
    public TicketClassificationResponse classifyWithKnowledge(
            @RequestBody TicketClassificationRequest request
    ) {
        TicketClassification classification =
                ragTicketClassifier.classify(request.message());

        return TicketClassificationResponse.from(classification);
    }
}