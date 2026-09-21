package com.ai_support_ticket_triage.ai.reranking;


import com.ai_support_ticket_triage.ai.vectors.HybridSearchResult;

public record RerankResult(
        HybridSearchResult result,
        double relevanceScore
) {}
