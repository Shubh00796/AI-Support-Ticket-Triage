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
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * Default document service implementation.
 *
 * <p>This service validates uploaded documents, parses their content,
 * normalizes extracted text, persists the document record,
 * and stores normalized derived chunks.</p>
 */
@Service
@RequiredArgsConstructor
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

    /**
     * Uploads and processes a document.
     *
     * @param file the uploaded document
     * @return the upload response
     */
    @Override
    public DocumentUploadResponse upload(MultipartFile file) {

        validateDocumentForNullCheck(file);

        // 1. Validate uploaded document
        ValidatedDocument validatedDocument =
                documentValidator.validate(file);

        // 2. Parse document into pages
        List<ParsedPage> pages =
                parseDocument(validatedDocument);

        // 3. Normalize each page
        List<ParsedPage> normalizedPages =
                normalizePages(pages);

        // 4. Build document-level text
        String rawText =
                extractRawText(pages);

        String normalizedText =
                extractRawText(normalizedPages);

        // 5. Create and persist document
        AiDocument document =
                documentMapper.toEntity(
                        validatedDocument,
                        rawText,
                        normalizedText
                );

        AiDocument savedDocument =
                documentRepository.save(document);

        // 6. Chunk normalized pages and persist chunks
        saveChunks(
                savedDocument.getId(),
                normalizedPages
        );

        // 7. Return response
        return documentMapper.toResponse(
                savedDocument,
                normalizedText.length()
        );
    }

    private static void validateDocumentForNullCheck(MultipartFile file) {
        if (file == null) {
            throw new IllegalArgumentException(
                    "Uploaded file must not be null"
            );
        }
    }

    /**
     * Resolves an appropriate parser and parses the validated document content.
     *
     * @param validatedDocument the validated upload metadata and content
     * @return parsed pages extracted from the document
     */
    private List<ParsedPage> parseDocument(
            ValidatedDocument validatedDocument
    ) {

        if (validatedDocument == null) {
            throw new IllegalArgumentException(
                    "Validated document must not be null"
            );
        }

        DocumentParser parser =
                parserResolver.resolve(
                        validatedDocument.detectedContentType()
                );

        return parser.parse(
                validatedDocument.content()
        );
    }

    /**
     * Normalizes each parsed page independently.
     *
     * <p>Keeping page boundaries intact is important because page numbers
     * are later stored as chunk metadata.</p>
     *
     * @param pages parsed document pages
     * @return normalized document pages
     */
    private List<ParsedPage> normalizePages(
            List<ParsedPage> pages
    ) {

        if (pages == null || pages.isEmpty()) {
            return List.of();
        }

        return pages.stream()
                .filter(page -> page != null)
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
    private String extractRawText(
            List<ParsedPage> pages
    ) {

        if (pages == null || pages.isEmpty()) {
            return "";
        }

        return pages.stream()
                .filter(page -> page != null)
                .map(ParsedPage::text)
                .filter(text -> text != null)
                .collect(Collectors.joining("\n\n"));
    }

    /**
     * Converts domain chunks into persistence entities and saves them.
     *
     * @param documentId persisted document identifier
     * @param pages normalized pages used to derive chunks
     */
    private void saveChunks(
            UUID documentId,
            List<ParsedPage> pages
    ) {

        if (documentId == null) {
            throw new IllegalArgumentException(
                    "Document ID must not be null"
            );
        }

        if (pages == null || pages.isEmpty()) {
            return;
        }

        List<DocumentChunk> chunks =
                documentChunker.chunk(
                        documentId,
                        pages
                );

        if (chunks.isEmpty()) {
            return;
        }

        List<DocumentChunkEntity> entities =
                chunks.stream()
                        .map(documentChunkMapper::toEntity)
                        .toList();

        documentChunkRepository.saveAll(entities);
    }
}