package com.ai_support_ticket_triage.ai.services.impls;

import com.ai_support_ticket_triage.ai.classification.TicketClassification;
import com.ai_support_ticket_triage.ai.entity.Ticket;
import com.ai_support_ticket_triage.ai.exceptions.TicketNotFoundException;
import com.ai_support_ticket_triage.ai.reposiotry.TicketRepository;
import com.ai_support_ticket_triage.ai.services.AiClassifier;
import com.ai_support_ticket_triage.ai.services.TicketClassificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
public class TicketClassificationServiceImpl
        implements TicketClassificationService {

    private final TicketRepository ticketRepository;
    private final AiClassifier aiClassifier;

    @Override
    public TicketClassification classifyTicket(UUID ticketId) {

        validateTicketId(ticketId);

        Ticket ticket = ticketRepository.findById(ticketId)
                .orElseThrow(() -> new TicketNotFoundException(ticketId));

        TicketClassification classification =
                aiClassifier.classify(ticket);

        ticket.applyClassification(classification);

        ticketRepository.save(ticket);

        return classification;
    }

    private static void validateTicketId(UUID ticketId) {

        if (ticketId == null) {
            throw new IllegalArgumentException(
                    "Ticket ID must not be null"
            );
        }
    }
}