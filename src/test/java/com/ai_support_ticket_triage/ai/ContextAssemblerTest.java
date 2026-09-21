package com.ai_support_ticket_triage.ai;


import com.ai_support_ticket_triage.ai.context.ContextAssembler;
import com.ai_support_ticket_triage.ai.context.KnowledgeContext;
import com.ai_support_ticket_triage.ai.reranking.RerankResult;
import com.ai_support_ticket_triage.ai.vectors.HybridSearchResult;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class ContextAssemblerTest {

    private final ContextAssembler assembler =
            new ContextAssembler();

    @Test
    void shouldAssembleKnowledgeContext() {

        UUID chunkId =
                UUID.randomUUID();

        UUID documentId =
                UUID.randomUUID();

        HybridSearchResult result =
                new HybridSearchResult(
                        chunkId,
                        documentId,
                        0,
                        1,
                        "Double charges should be verified using the transaction ID.",
                        0.8,
                        0.9,
                        0.83
                );

        RerankResult rerankResult =
                new RerankResult(
                        result,
                        0.95
                );

        KnowledgeContext context =
                assembler.assemble(
                        List.of(rerankResult)
                );

        assertNotNull(context);
        assertFalse(
                context.content().isBlank()
        );

        assertTrue(
                context.content()
                        .contains(
                                "Double charges"
                        )
        );

        assertTrue(
                context.content()
                        .contains(
                                "Page: 1"
                        )
        );
    }

    @Test
    void shouldReturnEmptyContextWhenNoResults() {

        KnowledgeContext context =
                assembler.assemble(
                        List.of()
                );

        assertEquals(
                "",
                context.content()
        );
    }
}
