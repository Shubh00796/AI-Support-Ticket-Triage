package com.ai_support_ticket_triage.ai.services;


import com.ai_support_ticket_triage.ai.classification.TicketClassification;
import com.ai_support_ticket_triage.ai.entity.Ticket;

public interface AiClassifier {

    TicketClassification classify(Ticket ticket);
}