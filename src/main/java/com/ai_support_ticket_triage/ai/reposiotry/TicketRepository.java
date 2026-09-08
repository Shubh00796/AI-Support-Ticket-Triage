package com.ai_support_ticket_triage.ai.reposiotry;


import com.ai_support_ticket_triage.ai.entity.Ticket;
import com.ai_support_ticket_triage.ai.enums.TicketStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface TicketRepository extends JpaRepository<Ticket, UUID> {

    List<Ticket> findByStatus(TicketStatus status);

}