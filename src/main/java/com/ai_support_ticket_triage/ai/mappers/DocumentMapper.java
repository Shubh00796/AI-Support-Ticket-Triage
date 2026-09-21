package com.ai_support_ticket_triage.ai.mappers;

import com.ai_support_ticket_triage.ai.dto.responce.DocumentUploadResponse;
import com.ai_support_ticket_triage.ai.entity.AiDocument;
import com.ai_support_ticket_triage.ai.validations.ValidatedDocument;
import org.springframework.stereotype.Component;

/**
 * Mapper for converting between document entities and DTOs.
 *
 * <p>Handles conversion from validated documents to JPA entities
 * and from entities to API response objects.</p>
 */
@Component
public class DocumentMapper {

    /**
     * Converts a validated document to a JPA entity.
     *
     * <p>Takes the validated document metadata and text content
     * and creates a persistence entity.</p>
     *
     * @param document the validated document
     * @param rawText the original parsed text
     * @param normalizedText the processed text
     * @return the document JPA entity
     */
    public AiDocument toEntity(
            final ValidatedDocument document,
            final String rawText,
            final String normalizedText
    ) {
        return new AiDocument(
                document.fileName(),
                document.detectedContentType(),
                document.content().length,
                rawText,
                normalizedText
        );
    }

    /**
     * Converts a persisted document entity to an API response.
     *
     * @param document the document entity
     * @param normalizedTextLength the length of normalized text
     * @return the document upload response DTO
     */
    public DocumentUploadResponse toResponse(
            final AiDocument document,
            final int normalizedTextLength
    ) {
        return new DocumentUploadResponse(
                document.getId(),
                document.getFileName(),
                document.getContentType(),
                document.getFileSize(),
                document.getRawText() == null ? 0 : document.getRawText().length(),
                normalizedTextLength,
                document.getCreatedAt()
        );
    }
}
