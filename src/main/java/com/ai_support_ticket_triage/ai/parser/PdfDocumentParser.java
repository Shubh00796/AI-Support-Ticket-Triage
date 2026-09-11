package com.ai_support_ticket_triage.ai.parser;


import com.ai_support_ticket_triage.ai.exceptions.DocumentParsingException;
import org.apache.pdfbox.Loader;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
import org.springframework.stereotype.Component;

@Component
public class PdfDocumentParser implements DocumentParser {

    private static final String CONTENT_TYPE = "application/pdf";

    @Override
    public boolean supports(String contentType) {
        return CONTENT_TYPE.equalsIgnoreCase(contentType);
    }

    @Override
    public String parse(byte[] content) {
        try (PDDocument document = Loader.loadPDF(content)) {
            return new PDFTextStripper().getText(document);
        } catch (Exception e) {
            throw new DocumentParsingException(
                    "Failed to parse PDF document"

            );
        }
    }
}