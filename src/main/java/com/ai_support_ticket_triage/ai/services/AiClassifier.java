package com.ai_support_ticket_triage.ai.services;

import com.ai_support_ticket_triage.ai.classification.TicketClassification;
import com.ai_support_ticket_triage.ai.entity.Ticket;

/**
 * Interface for AI-powered ticket classification.
 *
 * <p>Implementations should classify tickets into categories, priorities,
 * support teams, and sentiment using machine learning or LLM-based approaches.</p>
 */
public interface AiClassifier {

    /**
     * Classifies a support ticket using AI.
     *
     * @param ticket the ticket to classify
     * @return the classification result
     */
    TicketClassification classify(Ticket ticket);
}