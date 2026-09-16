package com.ai_support_ticket_triage.ai.chunks;


import java.util.List;
import java.util.UUID;

public record EmbeddedChunk(
        UUID chunkId,
        UUID documentId,
        int chunkIndex,
        int pageNumber,
        String text,
        List<Float> vector
) {
}