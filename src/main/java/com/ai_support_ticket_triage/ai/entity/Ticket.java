package com.ai_support_ticket_triage.ai.entity;

import com.ai_support_ticket_triage.ai.enums.TicketStatus;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "tickets")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Ticket {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false, length = 5000)
    private String message;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private TicketStatus status;

    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    public Ticket(String message) {
        this.message = message;
        this.status = TicketStatus.OPEN;
        this.createdAt = LocalDateTime.now();
    }

    public void updateMessage(String message) {
        this.message = message;
    }

    public void close() {
        this.status = TicketStatus.RESOLVED;
    }

    public void reopen() {
        this.status = TicketStatus.OPEN;
    }
}