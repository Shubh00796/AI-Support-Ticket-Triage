package com.ai_support_ticket_triage.ai.entity;


import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "documents")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Document {

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

    public Document(
            String fileName,
            String contentType,
            long fileSize,
            String rawText,
            String normalizedText
    ) {
        this.fileName = fileName;
        this.contentType = contentType;
        this.fileSize = fileSize;
        this.rawText = rawText;
        this.normalizedText = normalizedText;
        this.createdAt = LocalDateTime.now();
    }
}