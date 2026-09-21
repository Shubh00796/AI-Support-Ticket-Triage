package com.ai_support_ticket_triage.ai.reranking;



import com.ai_support_ticket_triage.ai.vectors.HybridSearchResult;

import java.util.List;

public interface Reranker {

    List<RerankResult> rerank(
            String query,
            List<HybridSearchResult> candidates,
            int topK
    );
}