package com.ai_support_ticket_triage.ai.services;


import com.ai_support_ticket_triage.ai.dto.responce.DocumentUploadResponse;
import org.springframework.web.multipart.MultipartFile;

/**
 * Handles document upload and processing.
 */
public interface DocumentService {

    /**
     * Uploads a document, parses its content, stores the document, and persists chunks.
     *
     * @param file the uploaded file
     * @return the upload response
     */
    DocumentUploadResponse upload(MultipartFile file);
}
