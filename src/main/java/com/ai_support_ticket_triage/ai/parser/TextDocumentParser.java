package com.ai_support_ticket_triage.ai.parser;


import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;

@Component
public class TextDocumentParser implements DocumentParser {

    private static final String CONTENT_TYPE = "text/plain";

    @Override
    public boolean supports(String contentType) {
        return CONTENT_TYPE.equalsIgnoreCase(contentType);
    }

    @Override
    public String parse(byte[] content) {
        return new String(content, StandardCharsets.UTF_8);
    }
}