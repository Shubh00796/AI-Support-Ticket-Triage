package com.ai_support_ticket_triage.ai.validations;

import com.ai_support_ticket_triage.ai.exceptions.DocumentValidationException;
import org.apache.tika.Tika;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

@Component
public class DocumentValidator {

    private static final long MAX_FILE_SIZE =
            10L * 1024 * 1024;

    private static final int MAX_FILE_NAME_LENGTH = 255;

    private static final Set<String> SUPPORTED_CONTENT_TYPES =
            Set.of(
                    "application/pdf",
                    "text/plain"
            );

    private static final Map<String, String> EXPECTED_EXTENSIONS =
            Map.of(
                    "application/pdf", ".pdf",
                    "text/plain", ".txt"
            );

    private final Tika tika = new Tika();

    public ValidatedDocument validate(MultipartFile file) {

        validateBasicProperties(file);

        String fileName =
                StringUtils.cleanPath(file.getOriginalFilename());

        validateFileName(fileName);

        byte[] content = readContent(file);

        String detectedContentType =
                detectContentType(content, fileName);

        validateDetectedType(detectedContentType);
        validateExtension(fileName, detectedContentType);

        return new ValidatedDocument(
                fileName,
                detectedContentType,
                content
        );
    }

    private void validateBasicProperties(MultipartFile file) {

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
    }

    private void validateFileName(String fileName) {

        if (!StringUtils.hasText(fileName)) {
            throw new DocumentValidationException(
                    "Document filename must not be blank"
            );
        }

        if (fileName.length() > MAX_FILE_NAME_LENGTH) {
            throw new DocumentValidationException(
                    "Document filename must not exceed 255 characters"
            );
        }

        if (fileName.contains("..")
                || fileName.contains("/")
                || fileName.contains("\\")) {

            throw new DocumentValidationException(
                    "Document filename contains an invalid path"
            );
        }
    }

    private byte[] readContent(MultipartFile file) {

        try {
            return file.getBytes();
        } catch (IOException exception) {
            throw new DocumentValidationException(
                    "Failed to read uploaded document",
                    exception
            );
        }
    }

    private String detectContentType(
            byte[] content,
            String fileName
    ) {

        try {
            return tika.detect(content, fileName);
        } catch (Exception exception) {
            throw new DocumentValidationException(
                    "Unable to determine document type",
                    exception
            );
        }
    }

    private void validateDetectedType(
            String detectedContentType
    ) {

        if (!SUPPORTED_CONTENT_TYPES.contains(
                detectedContentType
        )) {
            throw new DocumentValidationException(
                    "Unsupported document type: "
                            + detectedContentType
            );
        }
    }

    private void validateExtension(
            String fileName,
            String detectedContentType
    ) {

        String expectedExtension =
                EXPECTED_EXTENSIONS.get(detectedContentType);

        String normalizedFileName =
                fileName.toLowerCase(Locale.ROOT);

        if (expectedExtension == null
                || !normalizedFileName.endsWith(
                expectedExtension
        )) {
            throw new DocumentValidationException(
                    "Document extension does not match its actual content"
            );
        }
    }
}
