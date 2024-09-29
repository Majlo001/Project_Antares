package com.majlo.antares.model.reservation;

import com.majlo.antares.model.User;
import com.majlo.antares.model.events.Event;
import com.majlo.antares.model.location.Seat;
import com.majlo.antares.model.transaction.TransactionItem;
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

    /* If null, then the seat is not reserved */
    @ManyToOne
    @JoinColumn(name = "user_id", nullable = true)
    private User user;

    /* If null, then the seat is not bought */
    @OneToOne(mappedBy = "seatStatus", cascade = CascadeType.ALL)
    private TransactionItem transactionItem;

    private boolean isSeatUnavailable;
    private boolean isSeatReserved;

    private LocalDateTime reservationTime;
    private LocalDateTime expirationTime;
}
