package com.ai_support_ticket_triage.ai.entity;


import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Lob;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Entity
@Table(
        name = "document_chunks_ai",
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "uk_document_chunk_index",
                        columnNames = {
                                "document_id",
                                "chunk_index"
                        }
                )
        }
)
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class DocumentChunkEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(
            name = "document_id",
            nullable = false
    )
    private UUID documentId;

    @Column(
            name = "chunk_index",
            nullable = false
    )
    private int chunkIndex;

    @Column(
            name = "page_number",
            nullable = false
    )
    private int pageNumber;

    @Lob
    @Column(
            nullable = false,
            columnDefinition = "LONGTEXT"
    )
    private String text;

    public DocumentChunkEntity(
            UUID documentId,
            int chunkIndex,
            int pageNumber,
            String text
    ) {
        this.documentId = documentId;
        this.chunkIndex = chunkIndex;
        this.pageNumber = pageNumber;
        this.text = text;
    }
}