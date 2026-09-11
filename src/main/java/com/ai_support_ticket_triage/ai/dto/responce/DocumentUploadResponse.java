package com.ai_support_ticket_triage.ai.dto.responce;


import java.time.LocalDateTime;
import java.util.UUID;

public record DocumentUploadResponse(
        UUID id,
        String fileName,
        String contentType,
        long fileSize,
        int extractedTextLength,
        LocalDateTime createdAt
) {
}
