package com.ai_support_ticket_triage.ai;


import com.ai_support_ticket_triage.ai.ollama.OllamaClient;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertFalse;

@SpringBootTest
class OllamaClientTest {

    @Autowired
    private OllamaClient ollamaClient;

    @Test
    void shouldChatWithOllama() {

        String response = ollamaClient.chat("""
                You are a support ticket classifier.

                Classify this ticket:

                My payment was deducted but my order is still pending.

                Return a short response explaining what category
                this ticket belongs to.
                """);

        assertNotNull(response);
        assertFalse(response.isBlank());

        System.out.println("OLLAMA RESPONSE:");
        System.out.println(response);
    }
}