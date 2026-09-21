package com.ai_support_ticket_triage.ai.entity;

import com.ai_support_ticket_triage.ai.classification.TicketClassification;
import com.ai_support_ticket_triage.ai.enums.*;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Objects;
import java.util.UUID;

/**
 * JPA entity representing a support ticket.
 *
 * <p>Immutable after construction (except through designated methods)
 * with protected constructor to ensure proper encapsulation.</p>
 */
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

    /**
     * Creates a new ticket with the provided message.
     *
     * @param message the ticket message (non-blank)
     * @throws NullPointerException if message is null
     * @throws IllegalArgumentException if message is blank
     */
    public Ticket(final String message) {
        Objects.requireNonNull(message, "Ticket message must not be null");

        if (message.isBlank()) {
            throw new IllegalArgumentException("Ticket message must not be blank");
        }

        this.message = message;
        this.status = TicketStatus.OPEN;
        this.createdAt = LocalDateTime.now();
    }

    /**
     * Applies a classification to this ticket.
     *
     * <p>Updates the ticket's category, priority, team, sentiment,
     * and classification reason with values from the classification.</p>
     *
     * @param classification the classification to apply
     * @throws NullPointerException if classification is null
     */
    public void applyClassification(final TicketClassification classification) {
        Objects.requireNonNull(classification, "Ticket classification must not be null");

        this.category = classification.category();
        this.priority = classification.priority();
        this.team = classification.team();
        this.sentiment = classification.sentiment();
        this.classificationReason = classification.reason();
    }

    /**
     * Updates the ticket's message.
     *
     * @param message the new message
     */
    public void updateMessage(final String message) {
        this.message = message;
    }

    /**
     * Marks this ticket as resolved/closed.
     */
    public void close() {
        this.status = TicketStatus.RESOLVED;
    }

    /**
     * Reopens this ticket.
     */
    public void reopen() {
        this.status = TicketStatus.OPEN;
    }
}
