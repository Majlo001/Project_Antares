package com.majlo.antares.service.reservation;

import com.majlo.antares.dtos.reservation.SeatReservationRequestDto;
import com.majlo.antares.model.User;
import com.majlo.antares.model.reservation.EventSeatStatus;
import com.majlo.antares.repository.reservation.EventSeatStatusRepository;
import com.majlo.antares.service.UserService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class EventSeatStatusService {

    /** Expire time in minutes */
    @Value("${reservation.expireTime}")
    private Integer expireTime;

    private final EventSeatStatusRepository eventSeatStatusRepository;

    private final UserService userService;

    public EventSeatStatusService(EventSeatStatusRepository eventSeatStatusRepository, UserService userService) {
        this.eventSeatStatusRepository = eventSeatStatusRepository;
        this.userService = userService;
    }

    /**
     * Reserving seats for a user
     *
     * @param seatRequests list of seat reservation requests
     * @param userId       user id
     */
    @Transactional
    public void reserveSeats(List<SeatReservationRequestDto> seatRequests, Long userId) {
        for (SeatReservationRequestDto request : seatRequests) {
            EventSeatStatus seatStatus = eventSeatStatusRepository
                    .findBySeatIdAndEventId(request.getSeatId(), request.getEventId())
                    .orElseThrow(() -> new RuntimeException("Seat not available for reservation"));

            if (seatStatus.isSeatUnavailable() || seatStatus.isReserved() || seatStatus.isPaid()) {
                throw new RuntimeException("Seat is not available");
            }

            User user = userService.getUserById(userId);
            if (user == null) {
                throw new RuntimeException("User not found");
            }

            /** Update seat status */
            seatStatus.setUser(user);
            seatStatus.setReservationTime(LocalDateTime.now());
            seatStatus.setExpirationTime(LocalDateTime.now().plusMinutes(expireTime));

            eventSeatStatusRepository.save(seatStatus);
        }
    }

    /**
     * Releasing expired reservations
     */
    @Transactional
    public void releaseExpiredReservations() {
        LocalDateTime now = LocalDateTime.now();
        List<EventSeatStatus> expiredReservations = eventSeatStatusRepository.findByExpirationTimeBefore(now);
        for (EventSeatStatus seatStatus : expiredReservations) {
            seatStatus.setUser(null);
            seatStatus.setReservationTime(null);
            seatStatus.setExpirationTime(null);

            eventSeatStatusRepository.save(seatStatus);
        }
    }
}
