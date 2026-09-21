package com.ai_support_ticket_triage.ai.services.impls;

import com.ai_support_ticket_triage.ai.classification.TicketClassification;
import com.ai_support_ticket_triage.ai.entity.Ticket;
import com.ai_support_ticket_triage.ai.exceptions.TicketNotFoundException;
import com.ai_support_ticket_triage.ai.reposiotry.TicketRepository;
import com.ai_support_ticket_triage.ai.services.AiClassifier;
import com.ai_support_ticket_triage.ai.services.TicketClassificationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;
import java.util.UUID;

/**
 * Service implementation for classifying support tickets using AI.
 *
 * <p>Coordinates the classification process: retrieves the ticket, generates
 * a classification through an AI classifier, applies it to the ticket,
 * and persists the changes.</p>
 */
@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class TicketClassificationServiceImpl
        implements TicketClassificationService {

    private final TicketRepository ticketRepository;
    private final AiClassifier aiClassifier;

    /**
     * Classifies a ticket using AI and updates its classification.
     *
     * @param ticketId the ticket to classify
     * @return the ticket classification result
     * @throws TicketNotFoundException if the ticket does not exist
     */
    @Override
    public TicketClassification classifyTicket(UUID ticketId) {
        Objects.requireNonNull(ticketId, "Ticket ID must not be null");
        log.debug("Starting ticket classification. Ticket ID: {}", ticketId);

        final Ticket ticket = ticketRepository.findById(ticketId)
                .orElseThrow(() -> new TicketNotFoundException(ticketId));

        final TicketClassification classification = aiClassifier.classify(ticket);
        ticket.applyClassification(classification);
        ticketRepository.save(ticket);

        log.info("Ticket classified successfully. ID: {}, Category: {}, Priority: {}",
                ticketId, classification.category(), classification.priority());

        return classification;
    }
}