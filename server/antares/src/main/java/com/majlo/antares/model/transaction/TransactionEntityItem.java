package com.majlo.antares.model.transaction;

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

    // TODO: Create a separate table for ticket types eg. "Normal", "Student", "Senior"
    private String ticketType;


    private LocalDateTime purchaseDate;
}

