package com.ai_support_ticket_triage.ai.services.impls;


import com.ai_support_ticket_triage.ai.dto.responce.DocumentUploadResponse;
import com.ai_support_ticket_triage.ai.entity.Document;
import com.ai_support_ticket_triage.ai.exceptions.DocumentParsingException;
import com.ai_support_ticket_triage.ai.normalization.TextNormalizer;
import com.ai_support_ticket_triage.ai.parser.DocumentParser;
import com.ai_support_ticket_triage.ai.parser.DocumentParserResolver;
import com.ai_support_ticket_triage.ai.reposiotry.DocumentRepository;
import com.ai_support_ticket_triage.ai.services.DocumentService;
import com.ai_support_ticket_triage.ai.validations.DocumentValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

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

        documentValidator.validate(file);

        DocumentParser parser =
                parserResolver.resolve(file.getContentType());

        String rawText = parser.parse(readBytes(file));

        String normalizedText =
                textNormalizer.normalize(rawText);

        Document document =
                createDocument(file, rawText, normalizedText);

        Document savedDocument =
                documentRepository.save(document);

        return toUploadResponse(savedDocument);
    }



    private Document createDocument(
            MultipartFile file,
            String rawText,
            String normalizedText
    ) {
        return new Document(
                file.getOriginalFilename(),
                file.getContentType(),
                file.getSize(),
                rawText,
                normalizedText
        );
    }

    private DocumentUploadResponse toUploadResponse(Document document) {
        return new DocumentUploadResponse(
                document.getId(),
                document.getFileName(),
                document.getContentType(),
                document.getFileSize(),
                document.getNormalizedText().length(),
                document.getCreatedAt()
        );
    }

    private byte[] readBytes(MultipartFile file) {
        try {
            return file.getBytes();
        } catch (IOException e) {
            throw new DocumentParsingException(
                    "Failed to read uploaded document"

            );
        }
    }
}