package com.ai_support_ticket_triage.ai.mappers;


import com.ai_support_ticket_triage.ai.dto.responce.DocumentUploadResponse;
import com.ai_support_ticket_triage.ai.entity.AiDocument;
import com.ai_support_ticket_triage.ai.validations.ValidatedDocument;
import org.springframework.stereotype.Component;

@Component
public class DocumentMapper {

    public AiDocument toEntity(
            ValidatedDocument document,
            String rawText,
            String normalizedText
    ) {
        return new AiDocument(
                document.fileName(),
                document.detectedContentType(),
                document.content().length,
                rawText,
                normalizedText
        );
    }

    public DocumentUploadResponse toResponse(
            AiDocument document,
            int normalizedTextLength
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
