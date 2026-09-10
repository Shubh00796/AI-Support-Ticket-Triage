package com.ai_support_ticket_triage.ai.entity;

import com.ai_support_ticket_triage.ai.classification.TicketClassification;
import com.ai_support_ticket_triage.ai.enums.*;
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

    @Enumerated(EnumType.STRING)
    @Column(length = 20)
    private TicketCategory category;

    @Enumerated(EnumType.STRING)
    @Column(length = 20)
    private TicketPriority priority;

    @Enumerated(EnumType.STRING)
    @Column(length = 30)
    private SupportTeam team;

    @Enumerated(EnumType.STRING)
    @Column(length = 20)
    private TicketSentiment sentiment;

    @Column(length = 1000)
    private String classificationReason;

    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    public Ticket(String message) {
        this.message = message;
        this.status = TicketStatus.OPEN;
        this.createdAt = LocalDateTime.now();
    }

    public void applyClassification(TicketClassification classification) {

        if (classification == null) {
            throw new IllegalArgumentException(
                    "Ticket classification must not be null"
            );
        }

        this.category = classification.category();
        this.priority = classification.priority();
        this.team = classification.team();
        this.sentiment = classification.sentiment();
        this.classificationReason = classification.reason();
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