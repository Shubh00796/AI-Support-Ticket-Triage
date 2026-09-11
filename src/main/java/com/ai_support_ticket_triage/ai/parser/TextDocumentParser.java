package com.ai_support_ticket_triage.ai.parser;

import com.ai_support_ticket_triage.ai.exceptions.DocumentParsingException;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.util.List;

@Component
public class TextDocumentParser implements DocumentParser {

    private static final String TEXT_CONTENT_TYPE =
            "text/plain";

    @Override
    public boolean supports(String contentType) {
        return TEXT_CONTENT_TYPE.equals(contentType);
    }

    @Override
    public List<ParsedPage> parse(byte[] content) {

        try {
            String text = new String(
                    content,
                    StandardCharsets.UTF_8
            );

            return List.of(
                    new ParsedPage(1, text)
            );

        } catch (Exception e) {
            throw new DocumentParsingException(
                    "Failed to parse text document",
                    e
            );
        }
    }
}