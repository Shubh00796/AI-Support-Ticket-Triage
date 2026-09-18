package com.ai_support_ticket_triage.ai.vectors;

import com.ai_support_ticket_triage.ai.entity.DocumentChunkEntity;
import com.ai_support_ticket_triage.ai.reposiotry.DocumentChunkRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * Combines vector similarity and keyword matching results into a single ranked
 * search response.
 */
@Service
@RequiredArgsConstructor
public class HybridSearchService {

    private static final double VECTOR_WEIGHT = 0.7;
    private static final double KEYWORD_WEIGHT = 0.3;

    private final VectorSearchService vectorSearchService;
    private final KeywordSearchService keywordSearchService;
    private final DocumentChunkRepository documentChunkRepository;

    /**
     * Executes a hybrid search for the given query and returns the best matching
     * document chunks ordered by the combined score.
     *
     * @param query the user search query
     * @param topK the maximum number of results to return
     * @return the top ranked hybrid search results
     * @throws IllegalArgumentException if the query is blank or {@code topK} is not positive
     */
    public List<HybridSearchResult> search(
            String query,
            int topK
    ) {

        validateInput(query, topK);

        List<VectorSearchResult> vectorResults =
                vectorSearchService.search(query, topK);

        List<KeywordSearchResult> keywordResults =
                keywordSearchService.search(query);

        Map<UUID, VectorSearchResult> vectorByChunk =
                vectorResults.stream()
                        .collect(Collectors.toMap(
                                VectorSearchResult::chunkId,
                                Function.identity()
                        ));

        Map<UUID, KeywordSearchResult> keywordByChunk =
                keywordResults.stream()
                        .collect(Collectors.toMap(
                                KeywordSearchResult::chunkId,
                                Function.identity()
                        ));

        Set<UUID> candidateIds = new HashSet<>();

        candidateIds.addAll(vectorByChunk.keySet());
        candidateIds.addAll(keywordByChunk.keySet());

        Map<UUID, DocumentChunkEntity> chunksById =
                loadMissingChunks(
                        candidateIds,
                        keywordByChunk
                );

        return candidateIds.stream()
                .map(chunkId ->
                        combine(
                                chunkId,
                                vectorByChunk.get(chunkId),
                                keywordByChunk.get(chunkId),
                                chunksById.get(chunkId)
                        )
                )
                .sorted(
                        Comparator.comparingDouble(
                                HybridSearchResult::finalScore
                        ).reversed()
                )
                .limit(topK)
                .toList();
    }

    /**
     * Loads chunk entities only for candidates that are missing keyword search text.
     *
     * @param candidateIds all candidate chunk identifiers collected from both search strategies
     * @param keywordByChunk keyword search results indexed by chunk identifier
     * @return chunk entities indexed by identifier for candidates that require a repository lookup
     */
    private Map<UUID, DocumentChunkEntity> loadMissingChunks(
            Set<UUID> candidateIds,
            Map<UUID, KeywordSearchResult> keywordByChunk
    ) {

        List<UUID> missingTextIds =
                candidateIds.stream()
                        .filter(id -> !keywordByChunk.containsKey(id))
                        .toList();

        if (missingTextIds.isEmpty()) {
            return Map.of();
        }

        return documentChunkRepository
                .findByIdIn(missingTextIds)
                .stream()
                .collect(Collectors.toMap(
                        DocumentChunkEntity::getId,
                        Function.identity()
                ));
    }

    /**
     * Merges the vector and keyword result data for a single chunk into a unified result.
     *
     * @param chunkId the chunk identifier
     * @param vectorResult the vector search result for the chunk, if available
     * @param keywordResult the keyword search result for the chunk, if available
     * @param chunk the persisted chunk entity used as a text fallback, if available
     * @return the combined hybrid search result
     */
    private HybridSearchResult combine(
            UUID chunkId,
            VectorSearchResult vectorResult,
            KeywordSearchResult keywordResult,
            DocumentChunkEntity chunk
    ) {

        KeywordSearchResult requiredKeywordResult =
                vectorResult == null
                        ? Objects.requireNonNull(
                                keywordResult,
                                "Keyword result must be present when vector result is absent"
                        )
                        : keywordResult;

        double vectorScore =
                vectorResult != null
                        ? vectorResult.score()
                        : 0.0;

        double keywordScore =
                keywordResult != null
                        ? keywordResult.keywordScore()
                        : 0.0;

        double finalScore =
                (VECTOR_WEIGHT * vectorScore)
                        + (KEYWORD_WEIGHT * keywordScore);

        String text = getText(
                keywordResult,
                chunk
        );

        UUID documentId =
                vectorResult != null
                        ? vectorResult.documentId()
                        : requiredKeywordResult.documentId();

        int chunkIndex =
                vectorResult != null
                        ? vectorResult.chunkIndex()
                        : requiredKeywordResult.chunkIndex();

        int pageNumber =
                vectorResult != null
                        ? vectorResult.pageNumber()
                        : requiredKeywordResult.pageNumber();

        return new HybridSearchResult(
                chunkId,
                documentId,
                chunkIndex,
                pageNumber,
                text,
                vectorScore,
                keywordScore,
                finalScore
        );
    }

    /**
     * Resolves the chunk text, preferring keyword search text when available.
     *
     * @param keywordResult the keyword search result that may already contain the chunk text
     * @param chunk the persisted chunk entity used as a fallback source of text
     * @return the resolved text, or an empty string when no text is available
     */
    private String getText(
            KeywordSearchResult keywordResult,
            DocumentChunkEntity chunk
    ) {

        if (keywordResult != null) {
            return keywordResult.text();
        }

        if (chunk != null) {
            return chunk.getText();
        }

        return "";
    }

    /**
     * Validates the search request before performing any downstream operations.
     *
     * @param query the user search query
     * @param topK the maximum number of results to return
     * @throws IllegalArgumentException if the query is blank or {@code topK} is not positive
     */
    private void validateInput(
            String query,
            int topK
    ) {

        if (query == null || query.isBlank()) {
            throw new IllegalArgumentException(
                    "Search query must not be blank"
            );
        }

        if (topK <= 0) {
            throw new IllegalArgumentException(
                    "Top K must be greater than zero"
            );
        }
    }
}