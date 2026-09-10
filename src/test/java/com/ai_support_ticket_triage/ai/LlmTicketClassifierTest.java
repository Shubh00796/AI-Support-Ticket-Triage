package com.ai_support_ticket_triage.ai;


import com.ai_support_ticket_triage.ai.classification.TicketClassification;
import com.ai_support_ticket_triage.ai.entity.Ticket;
import com.ai_support_ticket_triage.ai.services.AiClassifier;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
class LlmTicketClassifierTest {

    @Autowired
    private AiClassifier aiClassifier;

    @Test
    void shouldClassifyTicket() {

        Ticket ticket = new Ticket(
                "My payment was deducted but my order is still pending."
        );

        TicketClassification classification =
                aiClassifier.classify(ticket);

        assertNotNull(classification);

        System.out.println("CLASSIFICATION:");
        System.out.println(classification);
    }
}