package com.ai_support_ticket_triage.ai.controller;


import com.ai_support_ticket_triage.ai.vectors.KnowledgeRetrievalService;
import com.ai_support_ticket_triage.ai.vectors.RetrievedChunk;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/knowledge")
@RequiredArgsConstructor
public class KnowledgeSearchController {

    private final KnowledgeRetrievalService retrievalService;

    @GetMapping("/search")
    public List<RetrievedChunk> search(
            @RequestParam String query,
            @RequestParam(defaultValue = "5") int topK
    ) {
        return retrievalService.retrieve(query, topK);
    }
}