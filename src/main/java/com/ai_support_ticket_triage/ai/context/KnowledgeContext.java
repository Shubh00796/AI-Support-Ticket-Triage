package com.ai_support_ticket_triage.ai.context;


/**
 * Holds the formatted knowledge base context used in a RAG prompt.
 *
 * @param content the assembled context content
 */
public record KnowledgeContext(
        String content
) {}