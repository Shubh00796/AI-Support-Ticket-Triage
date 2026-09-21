package com.ai_support_ticket_triage.ai.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Objects;
import java.util.UUID;

/**
 * JPA entity representing an uploaded document.
 *
 * <p>Stores both the raw and normalized text content of uploaded documents,
 * along with metadata for tracking and retrieval.</p>
 */
@Entity
@Table(name = "ai_documents")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class AiDocument {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false, length = 255)
    private String fileName;

    @Column(nullable = false, length = 100)
    private String contentType;

    @Column(nullable = false)
    private long fileSize;

    @Lob
    @Column(nullable = false, columnDefinition = "LONGTEXT")
    private String rawText;

    @Lob
    @Column(nullable = false, columnDefinition = "LONGTEXT")
    private String normalizedText;

    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    /**
     * Creates a new document with the provided metadata and content.
     *
     * @param fileName the original filename
     * @param contentType the document MIME type
     * @param fileSize the file size in bytes
     * @param rawText the original parsed text content
     * @param normalizedText the processed/normalized text content
     * @throws NullPointerException if any required parameter is null
     */
    public AiDocument(
            final String fileName,
            final String contentType,
            final long fileSize,
            final String rawText,
            final String normalizedText
    ) {
        this.fileName = Objects.requireNonNull(fileName, "fileName must not be null");
        this.contentType = Objects.requireNonNull(contentType, "contentType must not be null");
        this.fileSize = fileSize;
        this.rawText = Objects.requireNonNull(rawText, "rawText must not be null");
        this.normalizedText = Objects.requireNonNull(normalizedText, "normalizedText must not be null");
        this.createdAt = LocalDateTime.now();
    }
}