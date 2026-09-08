package com.ai_support_ticket_triage.ai.ollama;


import java.util.List;

public record OllamaChatRequest(
        String model,
        List<OllamaMessage> messages,
        boolean stream
) {
}