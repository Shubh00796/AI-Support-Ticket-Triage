package com.ai_support_ticket_triage.ai.vectors;


import com.ai_support_ticket_triage.ai.chunks.EmbeddedChunk;

import java.util.List;

public interface VectorStore {

    void save(EmbeddedChunk chunk);

    List<VectorSearchResult> search(
            List<Float> vector,
            int topK
    );
}
