package com.ai_support_ticket_triage.ai.reposiotry;


import com.ai_support_ticket_triage.ai.entity.AiDocument;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface DocumentRepository
        extends JpaRepository<AiDocument, UUID> {
}