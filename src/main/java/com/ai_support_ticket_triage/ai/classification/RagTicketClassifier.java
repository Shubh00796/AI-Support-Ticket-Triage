package com.ai_support_ticket_triage.ai.classification;


import com.ai_support_ticket_triage.ai.context.ContextAssembler;
import com.ai_support_ticket_triage.ai.context.KnowledgeContext;
import com.ai_support_ticket_triage.ai.ollama.OllamaClient;
import com.ai_support_ticket_triage.ai.reranking.RerankResult;
import com.ai_support_ticket_triage.ai.reranking.RerankingService;
import com.ai_support_ticket_triage.ai.vectors.HybridSearchResult;
import com.ai_support_ticket_triage.ai.vectors.HybridSearchService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Classifies support tickets by combining ticket text with retrieved knowledge base context.
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class RagTicketClassifier {

    private static final int RETRIEVAL_TOP_K = 10;
    private static final int CONTEXT_TOP_K = 3;

    private final HybridSearchService hybridSearchService;
    private final RerankingService rerankingService;
    private final ContextAssembler contextAssembler;
    private final RagClassificationPromptBuilder promptBuilder;
    private final OllamaClient ollamaClient;
    private final TicketClassificationResponseParser responseParser;

    /**
     * Classifies a ticket message using retrieval, reranking, prompt building, and LLM parsing.
     *
     * @param ticketMessage the customer support ticket message
     * @return the parsed ticket classification
     */
    public TicketClassification classify(
            String ticketMessage
    ) {

        validateTicket(ticketMessage);

        List<HybridSearchResult> candidates =
                hybridSearchService.search(
                        ticketMessage,
                        RETRIEVAL_TOP_K
                );

        List<RerankResult> reranked =
                rerankingService.rerank(
                        ticketMessage,
                        candidates,
                        CONTEXT_TOP_K
                );

        reranked.forEach(result ->
                log.info(
                        "Reranked chunk - score: {}, documentId: {}, page: {}, text: {}",
                        result.relevanceScore(),
                        result.result().documentId(),
                        result.result().pageNumber(),
                        result.result().text()
                )
        );

        KnowledgeContext context =
                contextAssembler.assemble(
                        reranked
                );

        String prompt =
                promptBuilder.build(
                        ticketMessage,
                        context
                );

        String response =
                ollamaClient.chat(prompt);

        return responseParser.parse(response);
    }

    /**
     * Validates the input ticket message.
     *
     * @param ticketMessage the ticket message to validate
     */
    private static void validateTicket(String ticketMessage) {
        if (ticketMessage == null
                || ticketMessage.isBlank()) {

            throw new IllegalArgumentException(
                    "Ticket message must not be blank"
            );
        }
    }
}