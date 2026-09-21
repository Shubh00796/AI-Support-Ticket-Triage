package com.ai_support_ticket_triage.ai.mappers;

import com.ai_support_ticket_triage.ai.dto.responce.TicketResponse;
import com.ai_support_ticket_triage.ai.entity.Ticket;
import org.mapstruct.Mapper;

/**
 * MapStruct mapper for converting Ticket entities to response DTOs.
 *
 * <p>Automatically generates the mapping implementation using MapStruct.</p>
 */
@Mapper(componentModel = "spring")
public interface TicketMapper {

    /**
     * Converts a Ticket entity to a TicketResponse DTO.
     *
     * @param entity the ticket entity
     * @return the ticket response DTO
     */
    TicketResponse toResponse(Ticket entity);
}