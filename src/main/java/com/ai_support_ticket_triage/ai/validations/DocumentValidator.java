package com.ai_support_ticket_triage.ai.validations;


import com.ai_support_ticket_triage.ai.exceptions.DocumentValidationException;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.util.Set;

@Component
public class DocumentValidator {

    private static final long MAX_FILE_SIZE =
            10 * 1024 * 1024;

    private static final Set<String> SUPPORTED_CONTENT_TYPES =
            Set.of(
                    "application/pdf",
                    "text/plain"
            );

    public void validate(MultipartFile file) {

        if (file == null || file.isEmpty()) {
            throw new DocumentValidationException(
                    "Document must not be empty"
            );
        }

        if (file.getSize() > MAX_FILE_SIZE) {
            throw new DocumentValidationException(
                    "Document size must not exceed 10 MB"
            );
        }

        String contentType = file.getContentType();

        if (!SUPPORTED_CONTENT_TYPES.contains(contentType)) {
            throw new DocumentValidationException(
                    "Unsupported document type: " + contentType
            );
        }

        if (file.getOriginalFilename() == null
                || file.getOriginalFilename().isBlank()) {

            throw new DocumentValidationException(
                    "Document filename must not be blank"
            );
        }
    }
}