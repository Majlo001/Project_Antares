package com.majlo.antares.service.reservation;

import com.majlo.antares.dtos.reservation.SeatReservationRequestDto;
import com.majlo.antares.model.User;
import com.majlo.antares.model.events.Event;
import com.majlo.antares.model.location.LocationVariant;
import com.majlo.antares.model.location.Row;
import com.majlo.antares.model.location.Seat;
import com.majlo.antares.model.location.Sector;
import com.majlo.antares.model.reservation.EventSeatStatus;
import com.majlo.antares.repository.events.EventRepository;
import com.majlo.antares.repository.location.SectorRepository;
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

    private final SectorRepository sectorRepository;
    private final EventSeatStatusRepository eventSeatStatusRepository;
    private final EventRepository eventRepository;
    private final UserService userService;

    public EventSeatStatusService(EventSeatStatusRepository eventSeatStatusRepository, EventRepository eventRepository, UserService userService, SectorRepository sectorRepository) {
        this.eventSeatStatusRepository = eventSeatStatusRepository;
        this.eventRepository = eventRepository;
        this.userService = userService;
        this.sectorRepository = sectorRepository;
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
    // TODO: Fix to make user can reserve seat without being logged in
    // TODO: Fix what happen when not enough seats are available
    @Transactional
    public List<Long> reserveSeats(List<SeatReservationRequestDto> seatRequests, Long userId, String sessionId) {
        List<Long> reservedSeatIds = new CopyOnWriteArrayList<>();
        List <EventSeatStatus> tempReservedSeats = new CopyOnWriteArrayList<>();

        if (seatRequests.isEmpty()) {
            throw new IllegalArgumentException("No seats requested for reservation.");
        }

        for (SeatReservationRequestDto request : seatRequests) {
            Long eventId = request.getEventId();
            EventSeatStatus seatStatus = null;

            Event event = eventRepository.findById(eventId)
                    .orElseThrow(() -> new RuntimeException("Event not found"));

            /** Reserve standing seats */
            if (request.getSeatId() == null) {
                Sector seatSector = sectorRepository.findById(request.getSectorId())
                        .orElseThrow(() -> new RuntimeException("Sector not found"));

                if (seatSector.isStanding() && seatSector.getStandingCapacity() != null && seatSector.getStandingCapacity() > 0) {
                    seatStatus = seatSector.findFirstAvailableStandingSeat();

                    if (seatStatus == null) {
                        throw new RuntimeException("No standing seats available for reservation");
                    }
                }
            }
            else {  /** Reserve seated seats */
                seatStatus = eventSeatStatusRepository
                        .findBySeatIdAndEventId(request.getSeatId(), request.getEventId())
                        .orElseThrow(() -> new RuntimeException("Seat not available for reservation"));
            }

            /** Check if user has exceeded the maximum number of reservations */
            User user = userService.getUserById(userId);
            int currentReservations = countUserReservations(eventId, user, sessionId);
            int maxReservations = event.getMaxReservationsPerUser();

            if (currentReservations + seatRequests.size() > maxReservations) {
                throw new RuntimeException("You have exceeded the maximum number of reservations for this event.");
            }

            /** Check if seat is available */
            if (seatStatus == null) {
                throw new RuntimeException("Seat is null");
            }
            if (seatStatus.isSeatUnavailable() || seatStatus.isReserved() || seatStatus.isPaid()) {
                throw new RuntimeException("Seat is not available");
            }


            /** Update seat status */
            if (user != null) {
                seatStatus.setUser(user);
                seatStatus.setSessionId(null);
            } else {
                seatStatus.setSessionId(sessionId);
                seatStatus.setUser(null);
            }

            seatStatus.setReservationTime(LocalDateTime.now());
            seatStatus.setExpirationTime(LocalDateTime.now().plusMinutes(expireTime));

            eventSeatStatusRepository.save(seatStatus);
            tempReservedSeats.add(seatStatus);
            reservedSeatIds.add(seatStatus.getId());
        }

        reservedSeats.addAll(tempReservedSeats);
        return reservedSeatIds;
    }

    /**
     * Releasing expired reservations
     */
    @Transactional
    public void releaseExpiredReservations() {
        LocalDateTime now = LocalDateTime.now();

        System.out.println("Count of reserved seats: " + reservedSeats.size());

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

    public void markAsPaid(Long seatStatusID) {
        reservedSeats.removeIf(seatStatus -> seatStatus.getId().equals(seatStatusID));
    }


    public void generateEventSeatStatuses(Event event) {
        LocationVariant eventLocationVariant = event.getLocationVariant();
        List<Sector> sectors = eventLocationVariant.getSectors();

        for (Sector sector : sectors) {
            if (!sector.isStanding()) {
                for (Row row : sector.getRows()) {
                    for (Seat seat : row.getSeats()) {
                        EventSeatStatus seatStatus = new EventSeatStatus();
                        seatStatus.setSeat(seat);
                        seatStatus.setEvent(event);
                        seatStatus.setSeatUnavailable(false);
                        seatStatus.setReservationTime(null);
                        seatStatus.setExpirationTime(null);
                        seatStatus.setSector(sector);
                        eventSeatStatusRepository.save(seatStatus);
                    }
                }
            } else {
                for (int i = 0; i < sector.getStandingCapacity(); i++) {
                    EventSeatStatus seatStatus = new EventSeatStatus();
                    seatStatus.setEvent(event);
                    seatStatus.setSeatUnavailable(false);
                    seatStatus.setReservationTime(null);
                    seatStatus.setExpirationTime(null);
                    seatStatus.setSector(sector);
                    eventSeatStatusRepository.save(seatStatus);
                }
            }
        }
    }
}
