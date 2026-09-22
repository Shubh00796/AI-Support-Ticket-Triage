package com.ai_support_ticket_triage.ai.vectors;

import com.ai_support_ticket_triage.ai.entity.DocumentChunkEntity;
import com.ai_support_ticket_triage.ai.reposiotry.DocumentChunkRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class KeywordSearchService {

    private static final int MAX_KEYWORDS = 5;

    private static final List<String> STOP_WORDS = List.of(
            "the",
            "a",
            "an",
            "is",
            "are",
            "was",
            "were",
            "my",
            "me",
            "but",
            "and",
            "or",
            "to",
            "of",
            "for",
            "in",
            "on",
            "this",
            "that",
            "still",
            "what",
            "should",
            "do",
            "if",
            "how",
            "can"
    );

    private static final Map<String, String> TERM_NORMALIZATION = Map.of(
            "charged", "charge",
            "charges", "charge",
            "charging", "charge",

            "twice", "double",
            "duplicate", "double",
            "duplicates", "double",

            "payments", "payment",
            "paid", "payment",

            "refunds", "refund",
            "refunded", "refund"
    );

    private final DocumentChunkRepository documentChunkRepository;

    public List<KeywordSearchResult> search(String query) {

        if (query == null || query.isBlank()) {
            throw new IllegalArgumentException(
                    "Search query must not be blank"
            );
        }

        List<String> keywords = extractKeywords(query);

        if (keywords.isEmpty()) {
            return List.of();
        }

        return keywords.stream()
                .flatMap(keyword ->
                        documentChunkRepository
                                .searchByKeyword(keyword)
                                .stream()
                )
                .distinct()
                .map(chunk -> toResult(chunk, keywords))
                .sorted(
                        (first, second) ->
                                Double.compare(
                                        second.keywordScore(),
                                        first.keywordScore()
                                )
                )
                .toList();
    }

    private List<String> extractKeywords(String query) {

        return Arrays.stream(
                        query.toLowerCase()
                                .trim()
                                .split("\\s+")
                )
                .map(word ->
                        word.replaceAll("[^a-z0-9]", "")
                )
                .filter(word -> !word.isBlank())
                .filter(word -> word.length() >= 3)
                .filter(word -> !STOP_WORDS.contains(word))
                .map(this::normalizeTerm)
                .distinct()
                .limit(MAX_KEYWORDS)
                .toList();
    }

    private String normalizeTerm(String word) {

        return TERM_NORMALIZATION.getOrDefault(
                word,
                word
        );
    }

    private KeywordSearchResult toResult(
            DocumentChunkEntity chunk,
            List<String> keywords
    ) {
        String normalizedText = normalizeText(chunk.getText());

        List<String> textWords = Arrays.asList(
                normalizedText.split("\\s+")
        );


        long matchedKeywords = keywords.stream()
                .filter(textWords::contains)
                .count();


        double keywordScore =
                (double) matchedKeywords / keywords.size();

        return new KeywordSearchResult(
                chunk.getId(),
                chunk.getDocumentId(),
                chunk.getChunkIndex(),
                chunk.getPageNumber(),
                chunk.getText(),
                keywordScore
        );
    }

    private String normalizeText(String text) {

        return Arrays.stream(
                        text.toLowerCase()
                                .split("\\s+")
                )
                .map(word ->
                        word.replaceAll("[^a-z0-9]", "")
                )
                .map(this::normalizeTerm)
                .reduce(
                        "",
                        (result, word) ->
                                result.isEmpty()
                                        ? word
                                        : result + " " + word
                );
    }
}