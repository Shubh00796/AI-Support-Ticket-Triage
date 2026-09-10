package com.ai_support_ticket_triage.ai.services;


import com.ai_support_ticket_triage.ai.classification.TicketClassification;

import java.util.UUID;

public interface TicketClassificationService {

    TicketClassification classifyTicket(UUID ticketId);
}