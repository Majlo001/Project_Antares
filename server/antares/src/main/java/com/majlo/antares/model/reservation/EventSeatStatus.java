package com.majlo.antares.model.reservation;

import com.majlo.antares.model.User;
import com.majlo.antares.model.events.Event;
import com.majlo.antares.model.location.Seat;
import com.majlo.antares.model.transaction.TransactionEntityItem;
import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Entity
@Data
@Builder
@Table(name = "event_seat_status")
public class EventSeatStatus {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "seat_id")
    private Seat seat;

    @ManyToOne
    @JoinColumn(name = "event_id")
    private Event event;

    /** Session ID for non logged users */
    private String sessionId;

    /** If null, then the seat is not reserved */
    @ManyToOne
    @JoinColumn(name = "user_id", nullable = true)
    private User user;

    /** If null, then the seat is not bought */
    @OneToOne(mappedBy = "seatStatus", cascade = CascadeType.ALL)
    private TransactionEntityItem transactionEntityItem;

    private boolean isSeatUnavailable;

    private LocalDateTime reservationTime;
    private LocalDateTime expirationTime;
//    private boolean paid;


    public boolean isReserved() {
        return (user != null || sessionId != null) && transactionEntityItem == null;
    }

    public boolean isPaid() {
        return transactionEntityItem != null;
    }
}
