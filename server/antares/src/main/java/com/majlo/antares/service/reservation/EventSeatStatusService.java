package com.majlo.antares.service.reservation;

import com.majlo.antares.dtos.reservation.SeatReservationRequestDto;
import com.majlo.antares.model.User;
import com.majlo.antares.model.events.Event;
import com.majlo.antares.model.reservation.EventSeatStatus;
import com.majlo.antares.repository.events.EventRepository;
import com.majlo.antares.repository.reservation.EventSeatStatusRepository;
import com.majlo.antares.service.UserService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

@Service
public class EventSeatStatusService {

    /** Expire time in minutes */
    @Value("${reservation.expireTime}")
    private Integer expireTime;

    /** List of reserved seats */
    private final List<EventSeatStatus> reservedSeats = new CopyOnWriteArrayList<>();


    private final EventSeatStatusRepository eventSeatStatusRepository;
    private final EventRepository eventRepository;
    private final UserService userService;

    public EventSeatStatusService(EventSeatStatusRepository eventSeatStatusRepository, EventRepository eventRepository,UserService userService) {
        this.eventSeatStatusRepository = eventSeatStatusRepository;
        this.eventRepository = eventRepository;
        this.userService = userService;
    }

    private int countUserReservations(Long eventId, User user, String sessionId) {
        int reservationCount = 0;
        for (EventSeatStatus seatStatus : reservedSeats) {
            if (seatStatus.getEvent().getId().equals(eventId) && seatStatus.getUser().getId().equals(user.getId())
                    || seatStatus.getEvent().getId().equals(eventId) && seatStatus.getSessionId().equals(sessionId)) {
                reservationCount++;
            }
        }
        return reservationCount;
    }

    /**
     * Reserving seats for a user
     *
     * @param seatRequests list of seat reservation requests
     * @param userId       user id
     */
    @Transactional
    public void reserveSeats(List<SeatReservationRequestDto> seatRequests, Long userId, String sessionId) {
        for (SeatReservationRequestDto request : seatRequests) {
            if (seatRequests.isEmpty()) {
                throw new IllegalArgumentException("No seats requested for reservation.");
            }

            Long eventId = seatRequests.get(0).getEventId();

            EventSeatStatus seatStatus = eventSeatStatusRepository
                    .findBySeatIdAndEventId(request.getSeatId(), request.getEventId())
                    .orElseThrow(() -> new RuntimeException("Seat not available for reservation"));

            Event event = eventRepository.findById(eventId)
                    .orElseThrow(() -> new RuntimeException("Event not found"));

            /** Check if user has exceeded the maximum number of reservations */
            User user = userService.getUserById(userId);
            int currentReservations = countUserReservations(eventId, user, sessionId);
            int maxReservations = event.getMaxReservationsPerUser();

            if (currentReservations + seatRequests.size() > maxReservations) {
                throw new RuntimeException("You have exceeded the maximum number of reservations for this event.");
            }

            /** Check if seat is available */
            if (seatStatus.isSeatUnavailable() || seatStatus.isReserved() || seatStatus.isPaid()) {
                throw new RuntimeException("Seat is not available");
            }


            /** Update seat status */
            if (user != null) {
                seatStatus.setUser(user);
                seatStatus.setSessionId(null);
            }
            else {
                seatStatus.setSessionId(sessionId);
                seatStatus.setUser(null);
            }

            seatStatus.setReservationTime(LocalDateTime.now());
            seatStatus.setExpirationTime(LocalDateTime.now().plusMinutes(expireTime));

            eventSeatStatusRepository.save(seatStatus);
            reservedSeats.add(seatStatus);
        }
    }

    /**
     * Releasing expired reservations
     */
    @Transactional
    public void releaseExpiredReservations() {
        LocalDateTime now = LocalDateTime.now();

        for (EventSeatStatus seatStatus : reservedSeats) {
            if (seatStatus.getExpirationTime().isBefore(now) && !seatStatus.isPaid()) {

                seatStatus.setUser(null);
                seatStatus.setSessionId(null);
                seatStatus.setReservationTime(null);
                seatStatus.setExpirationTime(null);

                eventSeatStatusRepository.save(seatStatus);
                reservedSeats.remove(seatStatus);
            }
        }
    }

    public void markAsPaid(Long seatStatusId) {
        EventSeatStatus seatStatus = eventSeatStatusRepository.findById(seatStatusId)
                .orElseThrow(() -> new RuntimeException("SeatStatus not found"));

        eventSeatStatusRepository.save(seatStatus);
        reservedSeats.remove(seatStatus);
    }
}
