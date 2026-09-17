package com.ai_support_ticket_triage.ai.vectors;


import com.ai_support_ticket_triage.ai.chunks.EmbeddedChunk;

public interface VectorStore {

    void save(EmbeddedChunk chunk);
}
