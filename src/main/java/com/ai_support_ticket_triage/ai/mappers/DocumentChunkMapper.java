package com.ai_support_ticket_triage.ai.mappers;


import com.ai_support_ticket_triage.ai.chunks.DocumentChunk;
import com.ai_support_ticket_triage.ai.entity.DocumentChunkEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface DocumentChunkMapper {

    DocumentChunkEntity toEntity(DocumentChunk chunk);
}
