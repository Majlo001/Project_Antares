package com.majlo.antares.schedulde;

import com.majlo.antares.service.reservation.EventSeatStatusService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class ReservationScheduler {

    private final EventSeatStatusService eventSeatStatusService;

    @Autowired
    public ReservationScheduler(EventSeatStatusService eventSeatStatusService) {
        this.eventSeatStatusService = eventSeatStatusService;
    }

    /** Check for expired reservations every minute */
    @Scheduled(fixedRate = 60000)
    public void checkExpiredReservations() {
        eventSeatStatusService.releaseExpiredReservations();
    }
}