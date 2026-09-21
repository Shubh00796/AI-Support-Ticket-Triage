package com.ai_support_ticket_triage.ai.controller;

import com.ai_support_ticket_triage.ai.dto.responce.DocumentUploadResponse;
import com.ai_support_ticket_triage.ai.services.DocumentService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

/**
 * REST controller for document upload and processing.
 *
 * <p>Provides endpoints for uploading documents, which triggers
 * parsing, normalization, chunking, and embedding generation.</p>
 */
@RestController
@RequestMapping("/api/documents")
@RequiredArgsConstructor
@Slf4j
public class DocumentController {

    private final DocumentService documentService;

    /**
     * Uploads a document and processes it through the pipeline.
     *
     * <p>The document is validated, parsed, normalized, chunked,
     * and embeddings are generated for semantic search.</p>
     *
     * @param file the document file to upload
     * @return the upload response with document metadata
     * @throws NullPointerException if file is null
     */
    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<DocumentUploadResponse> upload(
            @RequestParam("file") final MultipartFile file
    ) {
        log.info("Document upload request received. File: {}", file.getOriginalFilename());

        final DocumentUploadResponse response = documentService.upload(file);

        log.info("Document uploaded successfully. ID: {}", response.id());
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(response);
    }
}