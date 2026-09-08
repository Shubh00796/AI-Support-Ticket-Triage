package com.ai_support_ticket_triage.ai.mappers;

import com.ai_support_ticket_triage.ai.dto.responce.TicketResponse;
import com.ai_support_ticket_triage.ai.entity.Ticket;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface TicketMapper {

    TicketResponse toResponse(Ticket entity);
}