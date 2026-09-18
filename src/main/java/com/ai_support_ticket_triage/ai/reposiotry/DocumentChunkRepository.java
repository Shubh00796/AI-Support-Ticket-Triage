package com.ai_support_ticket_triage.ai.reposiotry;

import com.ai_support_ticket_triage.ai.entity.DocumentChunkEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface DocumentChunkRepository
        extends JpaRepository<DocumentChunkEntity, UUID> {

    List<DocumentChunkEntity> findByDocumentIdOrderByChunkIndex(
            UUID documentId
    );
    List<DocumentChunkEntity> findByIdIn(
            List<UUID> ids
    );

    @Query("""
    SELECT c
    FROM DocumentChunkEntity c
    WHERE c.text LIKE CONCAT('%', :keyword, '%')
""")
    List<DocumentChunkEntity> searchByKeyword(@Param("keyword") String keyword);
}