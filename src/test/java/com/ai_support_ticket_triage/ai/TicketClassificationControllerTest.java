package com.ai_support_ticket_triage.ai;

import com.ai_support_ticket_triage.ai.classification.TicketClassification;
import com.ai_support_ticket_triage.ai.controller.TicketClassificationController;
import com.ai_support_ticket_triage.ai.enums.SupportTeam;
import com.ai_support_ticket_triage.ai.enums.TicketCategory;
import com.ai_support_ticket_triage.ai.enums.TicketPriority;
import com.ai_support_ticket_triage.ai.enums.TicketSentiment;
import com.ai_support_ticket_triage.ai.services.TicketClassificationService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.UUID;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(TicketClassificationController.class)
class TicketClassificationControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private TicketClassificationService classificationService;

    @Test
    void shouldClassifyTicket() throws Exception {

        UUID ticketId = UUID.randomUUID();

        TicketClassification classification =
                new TicketClassification(
                        TicketCategory.PAYMENT,
                        TicketPriority.HIGH,
                        SupportTeam.BILLING,
                        TicketSentiment.FRUSTRATED,
                        "Payment was deducted but order is still pending."
                );

        when(classificationService.classifyTicket(ticketId))
                .thenReturn(classification);

        mockMvc.perform(
                        post("/api/tickets/{ticketId}/classify", ticketId)
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.category").value("PAYMENT"))
                .andExpect(jsonPath("$.priority").value("HIGH"))
                .andExpect(jsonPath("$.team").value("BILLING"))
                .andExpect(jsonPath("$.sentiment").value("FRUSTRATED"))
                .andExpect(jsonPath("$.reason")
                        .value("Payment was deducted but order is still pending."));
    }
}