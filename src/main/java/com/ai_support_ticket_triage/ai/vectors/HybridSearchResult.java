package com.ai_support_ticket_triage.ai.vectors;


import java.util.UUID;

public record HybridSearchResult(
        UUID chunkId,
        UUID documentId,
        int chunkIndex,
        int pageNumber,
        String text,
        double vectorScore,
        double keywordScore,
        double finalScore
) {
}