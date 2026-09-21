package com.ai_support_ticket_triage.ai.services.impls;

import com.ai_support_ticket_triage.ai.chunks.DocumentChunk;
import com.ai_support_ticket_triage.ai.chunks.DocumentChunker;
import com.ai_support_ticket_triage.ai.dto.responce.DocumentUploadResponse;
import com.ai_support_ticket_triage.ai.entity.AiDocument;
import com.ai_support_ticket_triage.ai.entity.DocumentChunkEntity;
import com.ai_support_ticket_triage.ai.mappers.DocumentChunkMapper;
import com.ai_support_ticket_triage.ai.mappers.DocumentMapper;
import com.ai_support_ticket_triage.ai.normalization.TextNormalizer;
import com.ai_support_ticket_triage.ai.parser.DocumentParser;
import com.ai_support_ticket_triage.ai.parser.DocumentParserResolver;
import com.ai_support_ticket_triage.ai.parser.ParsedPage;
import com.ai_support_ticket_triage.ai.reposiotry.DocumentChunkRepository;
import com.ai_support_ticket_triage.ai.reposiotry.DocumentRepository;
import com.ai_support_ticket_triage.ai.services.DocumentService;
import com.ai_support_ticket_triage.ai.validations.DocumentValidator;
import com.ai_support_ticket_triage.ai.validations.ValidatedDocument;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Objects;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * Service implementation for document upload and processing.
 *
 * <p>This service validates uploaded documents, parses their content,
 * normalizes extracted text, persists the document record,
 * and stores normalized derived chunks with embeddings.</p>
 *
 * <p>The processing pipeline follows these steps:
 * <ol>
 *   <li>Validate the uploaded document</li>
 *   <li>Parse document pages</li>
 *   <li>Normalize pages independently preserving boundaries</li>
 *   <li>Create and persist document entity</li>
 *   <li>Chunk normalized pages</li>
 *   <li>Generate and store embeddings for chunks</li>
 * </ol>
 * </p>
 */
@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class DocumentServiceImpl implements DocumentService {

    private final DocumentValidator documentValidator;
    private final DocumentParserResolver parserResolver;
    private final TextNormalizer textNormalizer;

    private final DocumentRepository documentRepository;
    private final DocumentMapper documentMapper;

    private final DocumentChunker documentChunker;
    private final DocumentChunkMapper documentChunkMapper;
    private final DocumentChunkRepository documentChunkRepository;
    private final DocumentEmbeddingService documentEmbeddingService;

    /**
     * Uploads and processes a document through the complete pipeline.
     *
     * @param file the uploaded document
     * @return the upload response containing document metadata
     * @throws NullPointerException if file is null
     * @throws org.springframework.web.multipart.MultipartException if upload fails
     */
    @Override
    public DocumentUploadResponse upload(MultipartFile file) {
        Objects.requireNonNull(file, "Uploaded file must not be null");
        log.info("Starting document upload. File: {}, Size: {} bytes", file.getOriginalFilename(), file.getSize());

        // 1. Validate uploaded document
        final ValidatedDocument validatedDocument = documentValidator.validate(file);
        log.debug("Document validation passed. Content type: {}", validatedDocument.detectedContentType());

        // 2. Parse document into pages
        final List<ParsedPage> pages = parseDocument(validatedDocument);
        log.debug("Document parsed into {} pages", pages.size());

        // 3. Normalize each page
        final List<ParsedPage> normalizedPages = normalizePages(pages);

        // 4. Build document-level text
        final String rawText = extractRawText(pages);
        final String normalizedText = extractRawText(normalizedPages);

        // 5. Create and persist document
        final AiDocument document = documentMapper.toEntity(validatedDocument, rawText, normalizedText);
        final AiDocument savedDocument = documentRepository.save(document);
        log.info("Document persisted. ID: {}, Content type: {}", savedDocument.getId(), validatedDocument.detectedContentType());

        // 6. Chunk normalized pages and persist chunks
        saveChunks(savedDocument.getId(), normalizedPages);

        // 7. Return response
        log.info("Document upload completed successfully. ID: {}", savedDocument.getId());
        return documentMapper.toResponse(savedDocument, normalizedText.length());
    }

    /**
     * Resolves an appropriate parser and parses the validated document content.
     *
     * @param validatedDocument the validated upload metadata and content
     * @return parsed pages extracted from the document
     */
    private List<ParsedPage> parseDocument(final ValidatedDocument validatedDocument) {
        Objects.requireNonNull(validatedDocument, "Validated document must not be null");

        final DocumentParser parser = parserResolver.resolve(validatedDocument.detectedContentType());
        return parser.parse(validatedDocument.content());
    }

    /**
     * Normalizes each parsed page independently.
     *
     * <p>Preserving page boundaries is important because page numbers
     * are later stored as chunk metadata for tracking document location.</p>
     *
     * @param pages parsed document pages
     * @return normalized document pages with boundaries preserved
     */
    private List<ParsedPage> normalizePages(final List<ParsedPage> pages) {
        if (pages == null || pages.isEmpty()) {
            return List.of();
        }

        return pages.stream()
                .filter(Objects::nonNull)
                .map(page -> new ParsedPage(
                        page.pageNumber(),
                        textNormalizer.normalize(page.text())
                ))
                .toList();
    }

    /**
     * Concatenates page text using page separators.
     *
     * @param pages parsed or normalized pages
     * @return combined document text
     */
    private String extractRawText(final List<ParsedPage> pages) {
        if (pages == null || pages.isEmpty()) {
            return "";
        }

        return pages.stream()
                .filter(Objects::nonNull)
                .map(ParsedPage::text)
                .filter(text -> text != null && !text.isBlank())
                .collect(Collectors.joining("\n\n"));
    }

    /**
     * Converts domain chunks into persistence entities and saves them with embeddings.
     *
     * @param documentId persisted document identifier
     * @param pages normalized pages used to derive chunks
     */
    private void saveChunks(final UUID documentId, final List<ParsedPage> pages) {
        Objects.requireNonNull(documentId, "Document ID must not be null");

        if (pages == null || pages.isEmpty()) {
            log.debug("No pages to chunk for document ID: {}", documentId);
            return;
        }

        final List<DocumentChunk> chunks = documentChunker.chunk(documentId, pages);
        if (chunks.isEmpty()) {
            log.warn("No chunks generated for document ID: {}", documentId);
            return;
        }

        final List<DocumentChunkEntity> entities = chunks.stream()
                .map(documentChunkMapper::toEntity)
                .toList();

        final List<DocumentChunkEntity> savedEntities = documentChunkRepository.saveAll(entities);
        log.debug("Saved {} chunks for document ID: {}", savedEntities.size(), documentId);

        documentEmbeddingService.embedChunks(chunks, savedEntities);
    }
}