package com.majlo.antares.repository.reservation;

import com.majlo.antares.model.reservation.EventSeatStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface EventSeatStatusRepository extends JpaRepository<EventSeatStatus, Long> {
    Optional<EventSeatStatus> findBySeatIdAndEventId(Long seatId, Long eventId);
    List<EventSeatStatus> findByExpirationTimeBefore(LocalDateTime expirationTime);
}
