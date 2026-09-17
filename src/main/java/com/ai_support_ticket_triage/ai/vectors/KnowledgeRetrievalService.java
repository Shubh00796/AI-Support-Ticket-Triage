package com.ai_support_ticket_triage.ai.vectors;


import com.ai_support_ticket_triage.ai.entity.DocumentChunkEntity;
import com.ai_support_ticket_triage.ai.reposiotry.DocumentChunkRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class KnowledgeRetrievalService {

    private final VectorSearchService vectorSearchService;
    private final DocumentChunkRepository documentChunkRepository;

    public List<RetrievedChunk> retrieve(
            String query,
            int topK
    ) {

        validateRequest(query, topK);

        List<VectorSearchResult> searchResults =
                vectorSearchService.search(query, topK);

        if (searchResults.isEmpty()) {
            return List.of();
        }

        List<UUID> chunkIds =
                searchResults.stream()
                        .map(VectorSearchResult::chunkId)
                        .toList();

        List<DocumentChunkEntity> chunks =
                documentChunkRepository.findByIdIn(chunkIds);

        Map<UUID, DocumentChunkEntity> chunksById =
                chunks.stream()
                        .collect(Collectors.toMap(
                                DocumentChunkEntity::getId,
                                Function.identity()
                        ));

        return searchResults.stream()
                .map(result -> toRetrievedChunk(result, chunksById))
                .flatMap(Optional::stream)
                .toList();
    }

    private void validateRequest(String query, int topK) {
        Objects.requireNonNull(query, "query must not be null");

        if (query.isBlank()) {
            throw new IllegalArgumentException("query must not be blank");
        }

        if (topK <= 0) {
            throw new IllegalArgumentException("topK must be greater than 0");
        }
    }

    private Optional<RetrievedChunk> toRetrievedChunk(
            VectorSearchResult result,
            Map<UUID, DocumentChunkEntity> chunksById
    ) {

        DocumentChunkEntity chunk =
                chunksById.get(result.chunkId());

        if (chunk == null) {
            return Optional.empty();
        }

        return Optional.of(new RetrievedChunk(
                chunk.getId(),
                chunk.getDocumentId(),
                chunk.getChunkIndex(),
                chunk.getPageNumber(),
                chunk.getText(),
                result.score()
        ));
    }
}