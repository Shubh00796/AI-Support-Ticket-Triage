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
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
public class TicketServiceImpl implements TicketService {

    private final TicketRepository ticketRepository;
    private final TicketMapper ticketMapper;

    @Override
    public TicketResponse createTicket(CreateTicketRequest request) {

        Ticket ticket = new Ticket(request.message());

        Ticket savedTicket = ticketRepository.save(ticket);

        return ticketMapper.toResponse(savedTicket);
    }

    @Override
    @Transactional(readOnly = true)
    public TicketResponse getTicket(UUID ticketId) {

        Ticket ticket = findTicket(ticketId);

        return ticketMapper.toResponse(ticket);
    }

    @Override
    @Transactional(readOnly = true)
    public List<TicketResponse> getAllTickets() {

        return ticketRepository.findAll()
                .stream()
                .map(ticketMapper::toResponse)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<TicketResponse> getTicketsByStatus(TicketStatus status) {

        if (status == null) {
            throw new IllegalArgumentException(
                    "Ticket status must not be null"
            );
        }

        return ticketRepository.findByStatus(status)
                .stream()
                .map(ticketMapper::toResponse)
                .toList();
    }



    @Override
    public void deleteTicket(UUID ticketId) {

        Ticket ticket = findTicket(ticketId);

        ticketRepository.delete(ticket);
    }

    private Ticket findTicket(UUID ticketId) {

        if (ticketId == null) {
            throw new IllegalArgumentException(
                    "Ticket ID must not be null"
            );
        }

        return ticketRepository.findById(ticketId)
                .orElseThrow(() -> new TicketNotFoundException(ticketId));
    }
}