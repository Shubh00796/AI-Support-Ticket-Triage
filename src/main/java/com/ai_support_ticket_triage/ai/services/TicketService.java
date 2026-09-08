package com.ai_support_ticket_triage.ai.services;


import com.ai_support_ticket_triage.ai.dto.request.CreateTicketRequest;
import com.ai_support_ticket_triage.ai.dto.responce.TicketResponse;
import com.ai_support_ticket_triage.ai.enums.TicketStatus;

import java.util.List;
import java.util.UUID;

public interface TicketService {

    TicketResponse createTicket(CreateTicketRequest request);

    TicketResponse getTicket(UUID ticketId);

    List<TicketResponse> getAllTickets();

    List<TicketResponse> getTicketsByStatus(TicketStatus status);

    void deleteTicket(UUID ticketId);
}