package com.ai_support_ticket_triage.ai.parser;

import com.ai_support_ticket_triage.ai.exceptions.UnsupportedDocumentTypeException;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class DocumentParserResolver {

    private final List<DocumentParser> parsers;

    public DocumentParserResolver(
            List<DocumentParser> parsers
    ) {
        this.parsers = List.copyOf(parsers);
    }

    public DocumentParser resolve(
            String detectedContentType
    ) {

        return parsers.stream()
                .filter(parser ->
                        parser.supports(
                                detectedContentType
                        )
                )
                .findFirst()
                .orElseThrow(() ->
                        new UnsupportedDocumentTypeException(
                                "Unsupported document type: "
                                        + detectedContentType
                        )
                );
    }
}