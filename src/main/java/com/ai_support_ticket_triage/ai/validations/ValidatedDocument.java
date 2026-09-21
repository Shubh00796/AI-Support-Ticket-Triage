package com.ai_support_ticket_triage.ai.validations;

/**
 * Immutable result of document validation.
 *
 * <p>Contains the validated filename, detected content type,
 * and the document content bytes after passing all validation checks.</p>
 *
 * @param fileName the cleaned and validated filename
 * @param detectedContentType the MIME type detected by Tika
 * @param content the raw document bytes
 */
public record ValidatedDocument(
        String fileName,
        String detectedContentType,
        byte[] content
) {
}
