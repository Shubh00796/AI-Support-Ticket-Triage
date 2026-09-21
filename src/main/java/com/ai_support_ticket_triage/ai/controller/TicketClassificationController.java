package com.ai_support_ticket_triage.ai.controller;

import com.ai_support_ticket_triage.ai.dto.responce.TicketClassificationResponse;
import com.ai_support_ticket_triage.ai.services.TicketClassificationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

/**
 * REST controller for ticket classification operations.
 *
 * <p>Provides endpoints for triggering AI-based classification
 * of support tickets to determine category, priority, and routing.</p>
 */
@RestController
@RequestMapping("/api/tickets")
@RequiredArgsConstructor
@Slf4j
public class TicketClassificationController {

    private final TicketClassificationService classificationService;

    /**
     * Classifies a support ticket using AI.
     *
     * <p>Analyzes the ticket message to determine its category,
     * priority, optimal support team, and sentiment.</p>
     *
     * @param ticketId the ticket to classify
     * @return the classification result
     * @throws com.ai_support_ticket_triage.ai.exceptions.TicketNotFoundException if ticket does not exist
     */
    @PostMapping("/{ticketId}/classify")
    public TicketClassificationResponse classifyTicket(
            @PathVariable final UUID ticketId
    ) {
        log.info("Received classification request for ticket: {}", ticketId);

        final TicketClassificationResponse response =
                TicketClassificationResponse.from(
                        classificationService.classifyTicket(ticketId)
                );

        log.info("Classification completed for ticket: {}", ticketId);
        return response;
    }
}