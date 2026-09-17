package com.ai_support_ticket_triage.ai.vectors;


import com.ai_support_ticket_triage.ai.embeddings.EmbeddingService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class VectorSearchService {

    private final EmbeddingService embeddingService;
    private final VectorStore vectorStore;

    public List<VectorSearchResult> search(
            String query,
            int topK
    ) {

        validateTopKAndQuery(query, topK);

        var embedding = embeddingService.embed(query);

        return vectorStore.search(
                embedding.vector(),
                topK
        );
    }

    private static void validateTopKAndQuery(String query, int topK) {
        if (query == null || query.isBlank()) {
            throw new IllegalArgumentException(
                    "Search query must not be blank"
            );
        }

        if (topK <= 0) {
            throw new IllegalArgumentException(
                    "Top K must be greater than zero"
            );
        }
    }
}
