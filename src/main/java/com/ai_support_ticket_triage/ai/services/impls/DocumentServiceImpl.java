package com.ai_support_ticket_triage.ai.services.impls;

import com.ai_support_ticket_triage.ai.dto.responce.DocumentUploadResponse;
import com.ai_support_ticket_triage.ai.entity.AiDocument;
import com.ai_support_ticket_triage.ai.normalization.TextNormalizer;
import com.ai_support_ticket_triage.ai.parser.DocumentParser;
import com.ai_support_ticket_triage.ai.parser.DocumentParserResolver;
import com.ai_support_ticket_triage.ai.parser.ParsedPage;
import com.ai_support_ticket_triage.ai.reposiotry.DocumentRepository;
import com.ai_support_ticket_triage.ai.services.DocumentService;
import com.ai_support_ticket_triage.ai.validations.DocumentValidator;
import com.ai_support_ticket_triage.ai.validations.ValidatedDocument;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class DocumentServiceImpl implements DocumentService {

    private final DocumentValidator documentValidator;
    private final DocumentParserResolver parserResolver;
    private final TextNormalizer textNormalizer;
    private final DocumentRepository documentRepository;

    @Override
    public DocumentUploadResponse upload(MultipartFile file) {

        ValidatedDocument validatedDocument =
                documentValidator.validate(file);

        DocumentParser parser =
                parserResolver.resolve(
                        validatedDocument.detectedContentType()
                );

        List<ParsedPage> pages =
                parser.parse(validatedDocument.content());

        String rawText =
                extractRawText(pages);

        String normalizedText =
                textNormalizer.normalize(rawText);

        AiDocument aiDocument =
                createDocument(
                        validatedDocument,
                        rawText,
                        normalizedText
                );

        AiDocument savedAiDocument =
                documentRepository.save(aiDocument);

        return toUploadResponse(savedAiDocument);
    }

    private String extractRawText(List<ParsedPage> pages) {

        return pages.stream()
                .map(ParsedPage::text)
                .collect(Collectors.joining("\n\n"));
    }

    private AiDocument createDocument(
            ValidatedDocument validatedDocument,
            String rawText,
            String normalizedText
    ) {
        return new AiDocument(
                validatedDocument.fileName(),
                validatedDocument.detectedContentType(),
                validatedDocument.content().length,
                rawText,
                normalizedText
        );
    }

    private DocumentUploadResponse toUploadResponse(
            AiDocument aiDocument
    ) {
        return new DocumentUploadResponse(
                aiDocument.getId(),
                aiDocument.getFileName(),
                aiDocument.getContentType(),
                aiDocument.getFileSize(),
                aiDocument.getNormalizedText().length(),
                aiDocument.getCreatedAt()
        );
    }
}