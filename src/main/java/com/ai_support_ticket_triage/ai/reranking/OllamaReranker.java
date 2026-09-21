package com.ai_support_ticket_triage.ai.reranking;


import com.ai_support_ticket_triage.ai.ollama.OllamaClient;
import com.ai_support_ticket_triage.ai.vectors.HybridSearchResult;
import lombok.RequiredArgsConstructor;
import org.jspecify.annotations.Nullable;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;

@Service
@RequiredArgsConstructor
public class OllamaReranker implements Reranker {

    private final OllamaClient ollamaClient;

    @Override
    public List<RerankResult> rerank(
            String query,
            List<HybridSearchResult> candidates,
            int topK
    ) {
        validateQuery(query);

        List<RerankResult> of = validateTopKAndCandidates(candidates, topK);
        if (of != null) return of;

        return candidates.stream()
                .map(candidate -> new RerankResult(
                        candidate,
                        calculateRelevance(query, candidate)
                ))
                .sorted(Comparator.comparingDouble(RerankResult::relevanceScore).reversed())
                .limit(topK)
                .toList();
    }

    private static @Nullable List<RerankResult> validateTopKAndCandidates(List<HybridSearchResult> candidates, int topK) {
        if (topK <= 0) {
            throw new IllegalArgumentException("Top K must be greater than zero");
        }

        if (candidates == null || candidates.isEmpty()) {
            return List.of();
        }
        return null;
    }

    private void validateQuery(String query) {
        if (query == null || query.isBlank()) {
            throw new IllegalArgumentException("Query must not be blank");
        }
    }

    private double calculateRelevance(
            String query,
            HybridSearchResult candidate
    ) {
        String prompt = """
            You are a document relevance evaluator.

            User query:
            %s

            Document:
            %s

            Give a relevance score from 0 to 1.

            1.0 = directly answers the user's query
            0.5 = somewhat related
            0.0 = unrelated

            Return ONLY the number.
            """.formatted(query, candidate.text());

        return parseScore(ollamaClient.chat(prompt));
    }

    private double parseScore(String response) {
        if (response == null || response.isBlank()) {
            return 0.0;
        }

        try {
            double score = Double.parseDouble(response.trim());

            if (!Double.isFinite(score)) {
                return 0.0;
            }

            return Math.clamp(score, 0.0, 1.0);

        } catch (NumberFormatException e) {
            return 0.0;
        }
    }
}
