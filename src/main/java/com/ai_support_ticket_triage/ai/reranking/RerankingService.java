package com.ai_support_ticket_triage.ai.reranking;


import com.ai_support_ticket_triage.ai.vectors.HybridSearchResult;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class RerankingService {

    private final Reranker reranker;

    public List<RerankResult> rerank(
            String query,
            List<HybridSearchResult> candidates,
            int topK
    ) {
        return reranker.rerank(
                query,
                candidates,
                topK
        );
    }
}
