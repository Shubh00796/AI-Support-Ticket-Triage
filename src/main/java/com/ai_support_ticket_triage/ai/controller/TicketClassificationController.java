package com.ai_support_ticket_triage.ai.controller;

import com.ai_support_ticket_triage.ai.dto.responce.TicketClassificationResponse;
import com.ai_support_ticket_triage.ai.services.TicketClassificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/tickets")
@RequiredArgsConstructor
public class TicketClassificationController {

    private final TicketClassificationService classificationService;

    @PostMapping("/{ticketId}/classify")
    public TicketClassificationResponse classifyTicket(
            @PathVariable UUID ticketId
    ) {
        return TicketClassificationResponse.from(
                classificationService.classifyTicket(ticketId)
        );
    }
}