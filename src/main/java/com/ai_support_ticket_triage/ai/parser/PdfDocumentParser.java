package com.ai_support_ticket_triage.ai.parser;

import com.ai_support_ticket_triage.ai.exceptions.DocumentParsingException;
import org.apache.pdfbox.Loader;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Component
public class PdfDocumentParser implements DocumentParser {

    private static final String PDF_CONTENT_TYPE =
            "application/pdf";

    @Override
    public boolean supports(String contentType) {
        return PDF_CONTENT_TYPE.equals(contentType);
    }

    @Override
    public List<ParsedPage> parse(byte[] content) {
        try (PDDocument document = Loader.loadPDF(content)) {
            PDFTextStripper stripper = new PDFTextStripper();
            List<ParsedPage> pages = new ArrayList<>();

            for (int page = 1; page <= document.getNumberOfPages(); page++) {
                stripper.setStartPage(page);
                stripper.setEndPage(page);
                pages.add(new ParsedPage(page, stripper.getText(document)));
            }

            return pages;
        } catch (IOException e) {
            throw new DocumentParsingException(
                    "Failed to parse PDF document",
                    e
            );
        }
    }
}
