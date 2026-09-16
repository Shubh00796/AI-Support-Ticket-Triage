package com.ai_support_ticket_triage.ai.parser;

import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Objects;

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
        Objects.requireNonNull(content, "content must not be null");

        return List.of(
                new ParsedPage(1, new String(content, StandardCharsets.UTF_8))
        );
    }
}
