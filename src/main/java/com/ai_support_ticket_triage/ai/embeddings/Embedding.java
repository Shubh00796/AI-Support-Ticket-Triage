package com.ai_support_ticket_triage.ai.embeddings;


import java.util.List;

public record Embedding(
        List<Float> vector
) {}