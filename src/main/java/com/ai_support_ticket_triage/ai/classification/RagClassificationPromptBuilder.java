package com.ai_support_ticket_triage.ai.classification;

import com.ai_support_ticket_triage.ai.context.KnowledgeContext;
import org.springframework.stereotype.Component;

/**
 * Builds the prompt used for RAG-based ticket classification.
 */
@Component
public class RagClassificationPromptBuilder {

    /**
     * Creates a prompt using the ticket message and optional knowledge context.
     *
     * @param ticketMessage the customer ticket message
     * @param knowledgeContext the retrieved knowledge context
     * @return the prompt sent to the language model
     */
    public String build(
            String ticketMessage,
            KnowledgeContext knowledgeContext
    ) {
        if (ticketMessage == null || ticketMessage.isBlank()) {
            throw new IllegalArgumentException(
                    "Ticket message must not be blank"
            );
        }

        String knowledge = knowledgeContext == null
                ? ""
                : knowledgeContext.content();

        return """
                You are an AI support ticket classification system.

                Your task is to classify the customer support ticket using
                the customer ticket as the primary source of truth and the
                knowledge base only as supporting context.

                ====================
                CORE RULES
                ====================

                1. Base the classification primarily on the CUSTOMER TICKET.

                2. Use the KNOWLEDGE BASE CONTEXT only when it is directly
                   relevant to the issue described in the ticket.

                3. If the knowledge base is empty, irrelevant, ambiguous,
                   contradictory, or unrelated to the ticket, ignore it.

                4. Never allow unrelated knowledge base information to change
                   the meaning or classification of the customer ticket.

                5. Do not invent facts, events, actions, policies, errors,
                   transactions, security incidents, or customer details.

                6. If information is missing, make the most conservative
                   classification supported by the available evidence.

                7. Classify the actual problem reported by the customer,
                   not merely words appearing in the knowledge base.

                8. Do not assume that a customer's mention of a word such as
                   "account", "payment", "order", "security", or "password"
                   automatically determines the category. Consider the
                   complete meaning of the ticket.

                9. Sentiment and priority are independent:
                   - A frustrated customer is not automatically HIGH priority.
                   - A calm customer can still have a CRITICAL issue.

                10. Do not increase priority merely because the ticket sounds
                    emotional.

                11. If the ticket contains multiple issues, identify the
                    primary issue that best represents the customer's main
                    problem.

                12. The reason must be based only on evidence contained in
                    the customer ticket and relevant knowledge base context.

                ====================
                CUSTOMER TICKET
                ====================

                %s

                ====================
                KNOWLEDGE BASE CONTEXT
                ====================

                %s

                ====================
                CLASSIFICATION VALUES
                ====================

                category MUST be exactly one of:

                - AUTHENTICATION
                - ACCOUNT
                - PAYMENT
                - GENERAL
                - TECHNICAL

                priority MUST be exactly one of:

                - LOW
                - MEDIUM
                - HIGH
                - CRITICAL

                team MUST be exactly one of:

                - BILLING_SUPPORT
                - TECHNICAL_SUPPORT
                - ACCOUNT_SUPPORT
                - GENERAL_SUPPORT

                sentiment MUST be exactly one of:

                - POSITIVE
                - NEUTRAL
                - FRUSTRATED
                - ANGRY

                ====================
                CLASSIFICATION GUIDANCE
                ====================

                AUTHENTICATION:
                Use when the primary problem involves authentication or
                inability to authenticate, such as login failure, password
                reset failure, invalid credentials, authentication failure,
                verification failure, or access authentication problems.

                ACCOUNT:
                Use when the primary problem concerns the customer's account
                itself, such as account settings, account status, profile
                information, account changes, or account-related requests
                that are not primarily authentication problems.

                PAYMENT:
                Use when the primary problem concerns money or payment
                transactions, such as duplicate charges, failed payments,
                incorrect charges, refunds, payment methods, transaction
                issues, or billing/payment disputes.

                TECHNICAL:
                Use when the primary problem is a technical malfunction,
                application/system failure, error, bug, integration issue,
                or technical behavior that is not primarily authentication,
                account, or payment related.

                GENERAL:
                Use when the ticket does not clearly belong to any of the
                specific categories above.

                ====================
                PRIORITY GUIDANCE
                ====================

                CRITICAL:
                Use only when the ticket describes a severe issue requiring
                immediate attention, such as a major security incident,
                widespread service outage, severe business impact, or another
                clearly urgent situation supported by the ticket.

                HIGH:
                Use when the issue has significant customer impact or prevents
                an important function from working, but does not clearly meet
                the CRITICAL criteria.

                MEDIUM:
                Use for normal support issues that require attention but do not
                indicate severe or immediate impact.

                LOW:
                Use for informational requests, minor issues, general
                questions, or issues with limited impact.

                Never infer severity that is not supported by the ticket.

                ====================
                SENTIMENT GUIDANCE
                ====================

                POSITIVE:
                The customer expresses satisfaction, appreciation, or clearly
                positive emotion.

                NEUTRAL:
                The customer states a problem or asks a question without
                clearly expressing negative or positive emotion.

                FRUSTRATED:
                The customer expresses annoyance, dissatisfaction, repeated
                unsuccessful attempts, or clear frustration.

                ANGRY:
                The customer expresses strong anger, hostility, outrage,
                threats, or extremely negative language.

                Do not classify a customer as ANGRY merely because they have
                a serious problem.

                ====================
                TEAM GUIDANCE
                ====================

                Assign the team that is most appropriate for resolving the
                PRIMARY issue.

                BILLING_SUPPORT:
                Use for payment, billing, charges, refunds, and transaction
                related issues.

                TECHNICAL_SUPPORT:
                Use for technical failures, application problems, bugs,
                system errors, integrations, and technical malfunctions.

                ACCOUNT_SUPPORT:
                Use for account management and authentication-related issues.

                GENERAL_SUPPORT:
                Use when no specialized team clearly applies.

                ====================
                AMBIGUOUS OR INCOMPLETE TICKETS
                ====================

                If the ticket is vague, incomplete, or ambiguous:

                - Do not invent missing details.
                - Use GENERAL when no specific category can be reliably
                  determined.
                - Use NEUTRAL when sentiment cannot be reliably determined.
                - Use MEDIUM when priority cannot be reliably determined but
                  the issue clearly requires normal support.
                - Assign the most appropriate team based on the available
                  evidence.

                ====================
                MULTIPLE ISSUES
                ====================

                If the ticket contains multiple issues:

                - Identify the primary issue based on the customer's main
                  request or most important problem.
                - Classify the ticket according to that primary issue.
                - Do not combine multiple enum values.
                - Mention only relevant supporting information in the reason.

                ====================
                OUTPUT REQUIREMENTS
                ====================

                Return EXACTLY ONE valid JSON object.

                The response MUST start with '{' and end with '}'.

                Do not write anything before the JSON.

                Do not write anything after the JSON.

                Do not use Markdown.

                Do not use code fences.

                Do not provide explanations outside the JSON.

                Do not add additional fields.

                Do not omit any required fields.

                All enum values MUST exactly match the values defined above.

                The required JSON structure is:

                {
                  "category": "AUTHENTICATION",
                  "priority": "MEDIUM",
                  "team": "ACCOUNT_SUPPORT",
                  "sentiment": "NEUTRAL",
                  "reason": "Brief evidence-based explanation."
                }

                The "reason" must be concise and must not introduce facts
                that are not supported by the customer ticket or relevant
                knowledge base context.

                Return only the JSON object.
                """
                .formatted(ticketMessage, knowledge);
    }
}