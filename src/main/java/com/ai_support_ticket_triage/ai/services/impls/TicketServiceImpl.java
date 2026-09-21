package com.ai_support_ticket_triage.ai.services.impls;

import com.ai_support_ticket_triage.ai.dto.request.CreateTicketRequest;
import com.ai_support_ticket_triage.ai.dto.responce.TicketResponse;
import com.ai_support_ticket_triage.ai.entity.Ticket;
import com.ai_support_ticket_triage.ai.enums.TicketStatus;
import com.ai_support_ticket_triage.ai.exceptions.TicketNotFoundException;
import com.ai_support_ticket_triage.ai.mappers.TicketMapper;
import com.ai_support_ticket_triage.ai.reposiotry.TicketRepository;
import com.ai_support_ticket_triage.ai.services.TicketService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;
import java.util.UUID;

/**
 * Service implementation for managing support tickets.
 *
 * <p>Handles ticket creation, retrieval, and deletion with proper logging
 * and transactional consistency. Validates all inputs and fails fast
 * with custom exceptions.</p>
 */
@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class TicketServiceImpl implements TicketService {

    private final TicketRepository ticketRepository;
    private final TicketMapper ticketMapper;

    /**
     * Creates a new support ticket.
     *
     * @param request ticket creation request containing the message
     * @return the created ticket response
     * @throws NullPointerException if request is null
     * @throws IllegalArgumentException if message is blank
     */
    @Override
    public TicketResponse createTicket(CreateTicketRequest request) {
        Objects.requireNonNull(request, "Ticket request must not be null");
        log.debug("Creating new ticket");

        final Ticket ticket = new Ticket(request.message());
        final Ticket savedTicket = ticketRepository.save(ticket);

        log.info("Ticket created successfully. ID: {}", savedTicket.getId());
        return ticketMapper.toResponse(savedTicket);
    }

    /**
     * Retrieves a ticket by ID.
     *
     * @param ticketId the ticket identifier
     * @return the ticket response
     * @throws TicketNotFoundException if the ticket does not exist
     */
    @Override
    @Transactional(readOnly = true)
    public TicketResponse getTicket(UUID ticketId) {
        final Ticket ticket = findTicketById(ticketId);
        log.debug("Ticket retrieved. ID: {}, Status: {}", ticketId, ticket.getStatus());
        return ticketMapper.toResponse(ticket);
    }

    /**
     * Retrieves all tickets.
     *
     * @return list of all ticket responses
     */
    @Override
    @Transactional(readOnly = true)
    public List<TicketResponse> getAllTickets() {
        log.debug("Retrieving all tickets");
        return ticketRepository.findAll()
                .stream()
                .map(ticketMapper::toResponse)
                .toList();
    }

    /**
     * Retrieves all tickets with a specific status.
     *
     * @param status the ticket status filter
     * @return list of ticket responses matching the status
     * @throws NullPointerException if status is null
     */
    @Override
    @Transactional(readOnly = true)
    public List<TicketResponse> getTicketsByStatus(TicketStatus status) {
        Objects.requireNonNull(status, "Ticket status must not be null");
        log.debug("Retrieving tickets with status: {}", status);
        return ticketRepository.findByStatus(status)
                .stream()
                .map(ticketMapper::toResponse)
                .toList();
    }

    /**
     * Deletes a ticket by ID.
     *
     * @param ticketId the ticket identifier
     * @throws TicketNotFoundException if the ticket does not exist
     */
    @Override
    public void deleteTicket(UUID ticketId) {
        final Ticket ticket = findTicketById(ticketId);
        ticketRepository.delete(ticket);
        log.info("Ticket deleted. ID: {}", ticketId);
    }

    /**
     * Finds a ticket by ID, raising an exception if not found.
     *
     * @param ticketId the ticket identifier
     * @return the found ticket
     * @throws TicketNotFoundException if the ticket does not exist
     */
    private Ticket findTicketById(UUID ticketId) {
        Objects.requireNonNull(ticketId, "Ticket ID must not be null");
        return ticketRepository.findById(ticketId)
                .orElseThrow(() -> new TicketNotFoundException(ticketId));
    }
}
