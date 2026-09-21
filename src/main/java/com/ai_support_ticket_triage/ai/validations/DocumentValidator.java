package com.ai_support_ticket_triage.ai.validations;

import com.ai_support_ticket_triage.ai.exceptions.DocumentValidationException;
import lombok.extern.slf4j.Slf4j;
import org.apache.tika.Tika;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

/**
 * Validator for uploaded documents.
 *
 * <p>Performs comprehensive validation including:
 * <ul>
 *   <li>File size constraints</li>
 *   <li>Filename validation</li>
 *   <li>Content type detection</li>
 *   <li>File extension verification</li>
 * </ul>
 * </p>
 */
@Component
@Slf4j
public class DocumentValidator {

    private static final long MAX_FILE_SIZE = 10L * 1024 * 1024; // 10 MB
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

    /**
     * Validates an uploaded document and returns its validated metadata.
     *
     * <p>Performs all validation checks and content type detection
     * in a single pass, returning the validated result.</p>
     *
     * @param file the uploaded document
     * @return validated document with detected content type and content bytes
     * @throws DocumentValidationException if any validation check fails
     */
    public ValidatedDocument validate(final MultipartFile file) {
        validateBasicProperties(file);

        final String originalFileName = file.getOriginalFilename();
        if (!StringUtils.hasText(originalFileName)) {
            log.warn("Document uploaded with blank filename");
            throw new DocumentValidationException(
                    "Document filename must not be blank"
            );
        }

        final String fileName = StringUtils.cleanPath(originalFileName);
        validateFileName(fileName);

        final byte[] content = readContent(file);
        final String detectedContentType = detectContentType(content, fileName);

        validateDetectedType(detectedContentType);
        validateExtension(fileName, detectedContentType);

        log.info("Document validation passed. File: {}, ContentType: {}", fileName, detectedContentType);
        return new ValidatedDocument(fileName, detectedContentType, content);
    }

    /**
     * Validates basic file properties (size, emptiness).
     *
     * @param file the file to validate
     * @throws DocumentValidationException if file is null, empty, or too large
     */
    private void validateBasicProperties(final MultipartFile file) {
        if (file == null || file.isEmpty()) {
            log.warn("Document upload attempted with null or empty file");
            throw new DocumentValidationException(
                    "Document must not be empty"
            );
        }

        if (file.getSize() > MAX_FILE_SIZE) {
            log.warn("Document upload rejected: file size {} exceeds limit {}", file.getSize(), MAX_FILE_SIZE);
            throw new DocumentValidationException(
                    "Document size must not exceed 10 MB"
            );
        }
    }

    /**
     * Validates the filename for length and path traversal attempts.
     *
     * @param fileName the cleaned filename
     * @throws DocumentValidationException if filename is invalid or too long
     */
    private void validateFileName(final String fileName) {
        if (!StringUtils.hasText(fileName)) {
            log.warn("Document rejected: filename is blank");
            throw new DocumentValidationException(
                    "Document filename must not be blank"
            );
        }

        if (fileName.length() > MAX_FILE_NAME_LENGTH) {
            log.warn("Document rejected: filename exceeds {} characters", MAX_FILE_NAME_LENGTH);
            throw new DocumentValidationException(
                    "Document filename must not exceed 255 characters"
            );
        }

        if (fileName.contains("..") || fileName.contains("/") || fileName.contains("\\")) {
            log.warn("Document rejected: filename contains invalid path characters");
            throw new DocumentValidationException(
                    "Document filename contains an invalid path"
            );
        }
    }

    /**
     * Reads the file content bytes.
     *
     * @param file the file to read
     * @return the file content as bytes
     * @throws DocumentValidationException if reading fails
     */
    private byte[] readContent(final MultipartFile file) {
        try {
            return file.getBytes();
        } catch (final IOException exception) {
            log.error("Failed to read uploaded document", exception);
            throw new DocumentValidationException(
                    "Failed to read uploaded document",
                    exception
            );
        }
    }

    /**
     * Detects the content type of the document using Tika.
     *
     * @param content the document bytes
     * @param fileName the original filename
     * @return detected MIME type
     * @throws DocumentValidationException if detection fails
     */
    private String detectContentType(
            final byte[] content,
            final String fileName
    ) {
        try {
            return tika.detect(content, fileName);
        } catch (final Exception exception) {
            log.error("Unable to determine document type for file: {}", fileName, exception);
            throw new DocumentValidationException(
                    "Unable to determine document type",
                    exception
            );
        }
    }

    /**
     * Validates that the detected content type is supported.
     *
     * @param detectedContentType the detected MIME type
     * @throws DocumentValidationException if type is not supported
     */
    private void validateDetectedType(final String detectedContentType) {
        Objects.requireNonNull(detectedContentType, "detectedContentType must not be null");

        if (!SUPPORTED_CONTENT_TYPES.contains(detectedContentType)) {
            log.warn("Document rejected: unsupported content type: {}", detectedContentType);
            throw new DocumentValidationException(
                    "Unsupported document type: " + detectedContentType
            );
        }
    }

    /**
     * Validates that the file extension matches the detected content type.
     *
     * @param fileName the filename
     * @param detectedContentType the detected MIME type
     * @throws DocumentValidationException if extension doesn't match detected type
     */
    private void validateExtension(
            final String fileName,
            final String detectedContentType
    ) {
        final String expectedExtension = EXPECTED_EXTENSIONS.get(detectedContentType);
        final String normalizedFileName = fileName.toLowerCase(Locale.ROOT);

        if (expectedExtension == null || !normalizedFileName.endsWith(expectedExtension)) {
            log.warn("Document rejected: extension mismatch for content type: {}", detectedContentType);
            throw new DocumentValidationException(
                    "Document extension does not match its actual content"
            );
        }
    }
}
