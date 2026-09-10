package com.ai_support_ticket_triage.ai.classification;


import com.ai_support_ticket_triage.ai.entity.Ticket;
import org.springframework.stereotype.Component;

@Component
public class TicketClassificationPromptBuilder {

    public String build(Ticket ticket) {

        return """
                You are a support ticket classification system.

                Your task is to classify the support ticket provided below.

                Allowed categories:
                - PAYMENT
                - AUTHENTICATION
                - TECHNICAL
                - ACCOUNT
                - GENERAL

                Allowed priorities:
                - LOW
                - MEDIUM
                - HIGH
                - CRITICAL

                Allowed support teams:
                - BILLING
                - TECHNICAL_SUPPORT
                - ACCOUNT_SUPPORT
                - GENERAL_SUPPORT

                Allowed sentiments:
                - POSITIVE
                - NEUTRAL
                - FRUSTRATED
                - ANGRY

                Ticket message:
                %s

                Return ONLY valid JSON in exactly this structure:

                {
                  "category": "PAYMENT",
                  "priority": "HIGH",
                  "team": "BILLING",
                  "sentiment": "FRUSTRATED",
                  "reason": "Brief explanation of the classification"
                }

                Rules:
                - Use only the allowed enum values.
                - Do not add additional fields.
                - Do not use markdown.
                - Do not include explanations outside the JSON.
                """.formatted(ticket.getMessage());
    }
}