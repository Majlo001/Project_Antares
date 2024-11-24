package com.majlo.antares.model.transaction;

import com.majlo.antares.model.location.TicketType;
import com.majlo.antares.model.reservation.EventSeatStatus;
import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Entity
@Data
@Builder
@Table(name = "transaction_item")
public class TransactionEntityItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "transaction_id")
    private TransactionEntity transactionEntity;

    @OneToOne
    @JoinColumn(name = "event_seat_status_id")
    private EventSeatStatus seatStatus;

    private double originalPrice;
    private double finalPrice;

    @ManyToOne
    @JoinColumn(name = "ticket_type_id")
    private TicketType ticketType;

    private LocalDateTime purchaseDate;

    @OneToOne
    @JoinColumn(name = "ticket_id")
    private Ticket ticket;
}

