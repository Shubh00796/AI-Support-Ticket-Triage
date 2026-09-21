package com.ai_support_ticket_triage.ai.dto.responce;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 * API response for document upload operations.
 *
 * <p>Contains metadata about the uploaded and processed document,
 * including identifiers, content type information, and processing results.</p>
 *
 * @param id the unique identifier of the uploaded document
 * @param fileName the original filename
 * @param contentType the detected MIME type
 * @param fileSize the size of the uploaded file in bytes
 * @param extractedTextLength the length of raw extracted text
 * @param normalizedTextLength the length of normalized text
 * @param createdAt the timestamp when the document was uploaded
 */
public record DocumentUploadResponse(
        UUID id,
        String fileName,
        String contentType,
        long fileSize,
        int extractedTextLength,
        int normalizedTextLength,
        LocalDateTime createdAt
) {
}
