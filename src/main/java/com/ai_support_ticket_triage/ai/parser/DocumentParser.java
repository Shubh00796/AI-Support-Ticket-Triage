package com.ai_support_ticket_triage.ai.parser;

import java.util.List;

public interface DocumentParser {

    boolean supports(String contentType);

    List<ParsedPage> parse(byte[] content);
}