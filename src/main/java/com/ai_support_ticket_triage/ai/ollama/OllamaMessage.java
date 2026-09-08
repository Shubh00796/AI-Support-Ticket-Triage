package com.ai_support_ticket_triage.ai.ollama;


public record OllamaMessage(
        String role,
        String content
) {
}