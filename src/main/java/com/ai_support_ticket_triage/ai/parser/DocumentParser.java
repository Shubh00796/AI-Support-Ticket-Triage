package com.ai_support_ticket_triage.ai.parser;

public interface DocumentParser {

    boolean supports(String contentType);

    String parse(byte[] content);
}