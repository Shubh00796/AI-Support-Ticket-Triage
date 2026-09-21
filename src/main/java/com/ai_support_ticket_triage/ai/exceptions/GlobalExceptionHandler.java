package com.ai_support_ticket_triage.ai.exceptions;

import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;
import java.util.stream.Collectors;

/**
 * Global exception handler for the application.
 *
 * <p>Handles both custom application exceptions and framework exceptions,
 * converting them into consistent, properly-formatted API error responses.</p>
 */
@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    /**
     * Handles resource-not-found exceptions.
     *
     * @param ex exception thrown when a resource cannot be found
     * @param request current HTTP request
     * @return a 404 error response
     */
    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ApiError> handleResourceNotFoundException(
            final ResourceNotFoundException ex,
            final HttpServletRequest request
    ) {
        log.warn("Resource not found: {}", ex.getMessage());
        final ApiError error = buildApiError(HttpStatus.NOT_FOUND, ex.getMessage(), request);
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
    }

    /**
     * Handles duplicate-resource exceptions.
     *
     * @param ex exception thrown when a duplicate resource is detected
     * @param request current HTTP request
     * @return a 409 error response
     */
    @ExceptionHandler(DuplicateResourceException.class)
    public ResponseEntity<ApiError> handleDuplicateResourceException(
            final DuplicateResourceException ex,
            final HttpServletRequest request
    ) {
        log.warn("Duplicate resource detected: {}", ex.getMessage());
        final ApiError error = buildApiError(HttpStatus.CONFLICT, ex.getMessage(), request);
        return ResponseEntity.status(HttpStatus.CONFLICT).body(error);
    }

    /**
     * Handles validation failures raised by Spring MVC.
     *
     * @param ex validation exception
     * @param request current HTTP request
     * @return a 400 error response containing validation messages
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiError> handleValidationException(
            final MethodArgumentNotValidException ex,
            final HttpServletRequest request
    ) {
        final String message = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(FieldError::getDefaultMessage)
                .collect(Collectors.joining(", "));

        log.warn("Validation failed: {}", message);
        final ApiError error = buildApiError(HttpStatus.BAD_REQUEST, message, request);
        return ResponseEntity.badRequest().body(error);
    }

    /**
     * Handles document validation exceptions.
     *
     * @param ex document validation exception
     * @param request current HTTP request
     * @return a 400 error response
     */
    @ExceptionHandler(DocumentValidationException.class)
    public ResponseEntity<ApiError> handleDocumentValidationException(
            final DocumentValidationException ex,
            final HttpServletRequest request
    ) {
        log.warn("Document validation failed: {}", ex.getMessage());
        final ApiError error = buildApiError(HttpStatus.BAD_REQUEST, ex.getMessage(), request);
        return ResponseEntity.badRequest().body(error);
    }

    /**
     * Handles unsupported document type exceptions.
     *
     * @param ex unsupported document type exception
     * @param request current HTTP request
     * @return a 415 error response
     */
    @ExceptionHandler(UnsupportedDocumentTypeException.class)
    public ResponseEntity<ApiError> handleUnsupportedDocumentTypeException(
            final UnsupportedDocumentTypeException ex,
            final HttpServletRequest request
    ) {
        log.warn("Unsupported document type: {}", ex.getMessage());
        final ApiError error = buildApiError(HttpStatus.UNSUPPORTED_MEDIA_TYPE, ex.getMessage(), request);
        return ResponseEntity.status(HttpStatus.UNSUPPORTED_MEDIA_TYPE).body(error);
    }

    /**
     * Handles illegal argument exceptions.
     *
     * @param ex illegal argument exception
     * @param request current HTTP request
     * @return a 400 error response
     */
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ApiError> handleIllegalArgumentException(
            final IllegalArgumentException ex,
            final HttpServletRequest request
    ) {
        log.warn("Invalid argument: {}", ex.getMessage());
        final ApiError error = buildApiError(HttpStatus.BAD_REQUEST, ex.getMessage(), request);
        return ResponseEntity.badRequest().body(error);
    }

    /**
     * Handles unexpected exceptions.
     *
     * @param ex unexpected exception
     * @param request current HTTP request
     * @return a 500 error response
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiError> handleGlobalException(
            final Exception ex,
            final HttpServletRequest request
    ) {
        log.error("Unexpected error occurred", ex);
        final ApiError error = buildApiError(HttpStatus.INTERNAL_SERVER_ERROR,
                "An unexpected error occurred", request);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
    }

    /**
     * Builds a standardized API error response with timestamp, status, and details.
     *
     * @param status HTTP status to use
     * @param message error message
     * @param request current HTTP request
     * @return standardized API error payload
     */
    private ApiError buildApiError(
            final HttpStatus status,
            final String message,
            final HttpServletRequest request
    ) {
        return ApiError.builder()
                .timestamp(LocalDateTime.now())
                .status(status.value())
                .error(status.getReasonPhrase())
                .message(message)
                .path(request.getRequestURI())
                .build();
    }
}