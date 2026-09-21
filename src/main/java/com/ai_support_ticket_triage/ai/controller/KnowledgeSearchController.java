package com.ai_support_ticket_triage.ai.controller;


import com.ai_support_ticket_triage.ai.reranking.RerankResult;
import com.ai_support_ticket_triage.ai.reranking.RerankingService;
import com.ai_support_ticket_triage.ai.vectors.*;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/knowledge")
@RequiredArgsConstructor
public class KnowledgeSearchController {

    private final KnowledgeRetrievalService retrievalService;
    private final KeywordSearchService keywordSearchService;
    private final HybridSearchService hybridSearchService;
    private final RerankingService rerankingService;


    @GetMapping("/search")
    public List<RetrievedChunk> search(
            @RequestParam String query,
            @RequestParam(defaultValue = "5") int topK
    ) {
        return retrievalService.retrieve(query, topK);
    }

    @GetMapping("/keyword-search")
    public List<KeywordSearchResult> keywordSearch(
            @RequestParam String query
    ) {
        return keywordSearchService.search(query);
    }

    @GetMapping("/hybrid-search")
    public List<HybridSearchResult> hybridSearch(
            @RequestParam String query,
            @RequestParam(defaultValue = "5") int topK
    ) {
        return hybridSearchService.search(query, topK);
    }

    @GetMapping("/rerank")
    public List<RerankResult> rerank(
            @RequestParam String query,
            @RequestParam(defaultValue = "5") int topK
    ) {

        List<HybridSearchResult> candidates =
                hybridSearchService.search(
                        query,
                        10
                );

        return rerankingService.rerank(
                query,
                candidates,
                topK
        );
    }

}