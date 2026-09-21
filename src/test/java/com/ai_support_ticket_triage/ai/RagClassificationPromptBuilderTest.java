package com.ai_support_ticket_triage.ai;


import com.ai_support_ticket_triage.ai.classification.RagClassificationPromptBuilder;
import com.ai_support_ticket_triage.ai.classification.RagClassificationPromptBuilder;
import com.ai_support_ticket_triage.ai.context.KnowledgeContext;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class RagClassificationPromptBuilderTest {

    private final RagClassificationPromptBuilder builder =
            new RagClassificationPromptBuilder();

    @Test
    void shouldBuildPromptWithTicketAndKnowledge() {

        String ticket =
                "My customer was charged twice for the same order.";

        KnowledgeContext context =
                new KnowledgeContext(
                        """
                        Double Charges / Duplicate Transactions:
                        Verify transaction IDs via Stripe or PayPal
                        before executing refunds.
                        """
                );

        String prompt =
                builder.build(
                        ticket,
                        context
                );

        assertTrue(
                prompt.contains(ticket)
        );

        assertTrue(
                prompt.contains(
                        "Double Charges / Duplicate Transactions"
                )
        );

        assertTrue(
                prompt.contains(
                        "Verify transaction IDs"
                )
        );

        assertTrue(
                prompt.contains(
                        "Return ONLY valid JSON"
                )
        );
    }

    @Test
    void shouldBuildPromptWithoutKnowledge() {

        String ticket =
                "I cannot log into my account.";

        KnowledgeContext context =
                new KnowledgeContext("");

        String prompt =
                builder.build(
                        ticket,
                        context
                );

        assertTrue(
                prompt.contains(ticket)
        );

        assertTrue(
                prompt.contains(
                        "KNOWLEDGE BASE CONTEXT"
                )
        );
    }
}