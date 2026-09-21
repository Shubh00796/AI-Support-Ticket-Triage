package com.ai_support_ticket_triage.ai.mappers;

import com.ai_support_ticket_triage.ai.chunks.DocumentChunk;
import com.ai_support_ticket_triage.ai.entity.DocumentChunkEntity;
import org.mapstruct.Mapper;

/**
 * MapStruct mapper for converting document chunks to persistence entities.
 *
 * <p>Automatically generates the mapping implementation using MapStruct.</p>
 */
@Mapper(componentModel = "spring")
public interface DocumentChunkMapper {

    /**
     * Converts a domain DocumentChunk to a persistence entity.
     *
     * @param chunk the domain chunk object
     * @return the persisted chunk entity
     */
    DocumentChunkEntity toEntity(DocumentChunk chunk);
}
