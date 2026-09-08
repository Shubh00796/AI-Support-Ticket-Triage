package com.ai_support_ticket_triage.ai.controller;

import com.ai_support_ticket_triage.ai.dto.request.CreateTicketRequest;
import com.ai_support_ticket_triage.ai.dto.responce.TicketResponse;
import com.ai_support_ticket_triage.ai.enums.TicketStatus;
import com.ai_support_ticket_triage.ai.services.TicketService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/tickets")
@RequiredArgsConstructor
public class TicketController {

    private final TicketService ticketService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public TicketResponse createTicket(
            @Valid @RequestBody CreateTicketRequest request
    ) {
        return ticketService.createTicket(request);
    }

    @GetMapping("/{ticketId}")
    public TicketResponse getTicket(
            @PathVariable UUID ticketId
    ) {
        return ticketService.getTicket(ticketId);
    }

    @GetMapping
    public List<TicketResponse> getAllTickets() {
        return ticketService.getAllTickets();
    }

    @GetMapping("/status/{status}")
    public List<TicketResponse> getTicketsByStatus(
            @PathVariable TicketStatus status
    ) {
        return ticketService.getTicketsByStatus(status);
    }


    @DeleteMapping("/{ticketId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteTicket(
            @PathVariable UUID ticketId
    ) {
        ticketService.deleteTicket(ticketId);
    }
}