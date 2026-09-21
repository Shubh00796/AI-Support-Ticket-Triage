package com.ai_support_ticket_triage.ai.controller;

import com.ai_support_ticket_triage.ai.dto.request.CreateTicketRequest;
import com.ai_support_ticket_triage.ai.dto.responce.TicketResponse;
import com.ai_support_ticket_triage.ai.enums.TicketStatus;
import com.ai_support_ticket_triage.ai.services.TicketService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

/**
 * REST controller for managing support tickets.
 *
 * <p>Provides endpoints for creating, retrieving, and deleting tickets.
 * All endpoints support proper HTTP status codes and validation.</p>
 */
@RestController
@RequestMapping("/api/tickets")
@RequiredArgsConstructor
@Slf4j
public class TicketController {

    private final TicketService ticketService;

    /**
     * Creates a new support ticket.
     *
     * @param request the ticket creation request (validated)
     * @return the created ticket
     * @throws IllegalArgumentException if request is invalid
     */
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public TicketResponse createTicket(
            @Valid @RequestBody final CreateTicketRequest request
    ) {
        log.info("Received request to create ticket");
        final TicketResponse response = ticketService.createTicket(request);
        log.info("Ticket created successfully: {}", response.id());
        return response;
    }

    /**
     * Retrieves a ticket by its unique identifier.
     *
     * @param ticketId the ticket identifier
     * @return the ticket details
     * @throws com.ai_support_ticket_triage.ai.exceptions.TicketNotFoundException if ticket does not exist
     */
    @GetMapping("/{ticketId}")
    public TicketResponse getTicket(
            @PathVariable final UUID ticketId
    ) {
        log.debug("Retrieving ticket: {}", ticketId);
        return ticketService.getTicket(ticketId);
    }

    /**
     * Retrieves all tickets.
     *
     * @return list of all tickets
     */
    @GetMapping
    public List<TicketResponse> getAllTickets() {
        log.debug("Retrieving all tickets");
        return ticketService.getAllTickets();
    }

    /**
     * Retrieves all tickets with a specific status.
     *
     * @param status the ticket status to filter by
     * @return list of tickets with the specified status
     */
    @GetMapping("/status/{status}")
    public List<TicketResponse> getTicketsByStatus(
            @PathVariable final TicketStatus status
    ) {
        log.debug("Retrieving tickets with status: {}", status);
        return ticketService.getTicketsByStatus(status);
    }

    /**
     * Deletes a ticket by its unique identifier.
     *
     * @param ticketId the ticket identifier
     * @throws com.ai_support_ticket_triage.ai.exceptions.TicketNotFoundException if ticket does not exist
     */
    @DeleteMapping("/{ticketId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteTicket(
            @PathVariable final UUID ticketId
    ) {
        log.info("Deleting ticket: {}", ticketId);
        ticketService.deleteTicket(ticketId);
    }
}