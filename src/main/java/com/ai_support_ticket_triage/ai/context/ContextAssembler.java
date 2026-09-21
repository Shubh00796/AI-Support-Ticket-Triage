package com.ai_support_ticket_triage.ai.context;


import com.ai_support_ticket_triage.ai.reranking.RerankResult;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Builds a text context block from reranked knowledge results.
 */
@Component
public class ContextAssembler {

    /**
     * Converts reranked results into a formatted knowledge context string.
     *
     * @param results the reranked knowledge results
     * @return the assembled knowledge context
     */
    public KnowledgeContext assemble(
            List<RerankResult> results
    ) {

        if (results == null || results.isEmpty()) {
            return new KnowledgeContext("");
        }

        StringBuilder context =
                new StringBuilder();

        context.append("KNOWLEDGE BASE CONTEXT\n\n");

        for (int i = 0; i < results.size(); i++) {

            RerankResult result =
                    results.get(i);

            context
                    .append("[Knowledge Chunk ")
                    .append(i + 1)
                    .append("]\n");

            context
                    .append("Document ID: ")
                    .append(
                            result.result().documentId()
                    )
                    .append("\n");

            context
                    .append("Page: ")
                    .append(
                            result.result().pageNumber()
                    )
                    .append("\n");

            context
                    .append("Content:\n")
                    .append(
                            result.result().text()
                    )
                    .append("\n\n");
        }

        return new KnowledgeContext(
                context.toString().trim()
        );
    }
}