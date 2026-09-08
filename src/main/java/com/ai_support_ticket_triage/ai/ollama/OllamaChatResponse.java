package com.ai_support_ticket_triage.ai.ollama;


public record OllamaChatResponse(
        String model,
        OllamaMessage message
) {
}