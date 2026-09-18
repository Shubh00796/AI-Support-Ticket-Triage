package com.ai_support_ticket_triage.ai.vectors;

import java.util.UUID;

public record KeywordSearchResult(
        UUID chunkId,
        UUID documentId,
        int chunkIndex,
        int pageNumber,
        String text,
        double keywordScore
) {
}