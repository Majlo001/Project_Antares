package com.majlo.antares.controller;

import com.majlo.antares.model.events.Event;
import com.majlo.antares.model.events.EventSeries;
import com.majlo.antares.model.events.EventStatus;
import com.majlo.antares.model.location.*;
import com.majlo.antares.repository.EventOwnerRepository;
import com.majlo.antares.repository.events.EventStatusRepository;
import com.majlo.antares.repository.events.EventSeriesRepository;
import com.majlo.antares.repository.events.EventRepository;
import com.majlo.antares.repository.location.LocationRepository;
import com.majlo.antares.repository.location.LocationVariantRepository;
import com.majlo.antares.repository.location.SectorRepository;
import com.majlo.antares.repository.location.TicketPriceRepository;
import com.majlo.antares.repository.location.TicketTypeRepository;
import jakarta.transaction.Transactional;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@RestController
@RequestMapping("/api/create")
public class CreateController {
    private final EventStatusRepository eventStatusRepository;
    private final LocationRepository locationRepository;
    private final LocationVariantRepository locationVariantRepository;
    private final SectorRepository sectorRepository;
    private final TicketPriceRepository ticketPriceRepository;
    private final EventSeriesRepository eventSeriesRepository;
    private final EventRepository eventRepository;
    private final TicketTypeRepository ticketTypeRepository;
    private final EventOwnerRepository eventOwnerRepository;

    public CreateController(EventStatusRepository eventStatusRepository, LocationRepository locationRepository, LocationVariantRepository locationVariantRepository, SectorRepository sectorRepository, TicketPriceRepository ticketPriceRepository, EventSeriesRepository eventSeriesRepository, EventRepository eventRepository, TicketTypeRepository ticketTypeRepository, EventOwnerRepository eventOwnerRepository) {
        this.eventStatusRepository = eventStatusRepository;
        this.locationRepository = locationRepository;
        this.locationVariantRepository = locationVariantRepository;
        this.sectorRepository = sectorRepository;
        this.ticketPriceRepository = ticketPriceRepository;
        this.eventSeriesRepository = eventSeriesRepository;
        this.eventRepository = eventRepository;
        this.ticketTypeRepository = ticketTypeRepository;
        this.eventOwnerRepository = eventOwnerRepository;
    }

    @PostMapping("/statuses")
    public void createStatuses() {
        List<EventStatus> statuses = Arrays.asList(
                EventStatus.builder().eventStatusName("Planned").build(),
                EventStatus.builder().eventStatusName("Scheduled").build(),
                EventStatus.builder().eventStatusName("Public").build(),
                EventStatus.builder().eventStatusName("Sold out").build(),
                EventStatus.builder().eventStatusName("Finished").build(),
                EventStatus.builder().eventStatusName("Cancelled").build(),
                EventStatus.builder().eventStatusName("Postponed").build()
        );

        eventStatusRepository.saveAll(statuses);
    }

    @PostMapping("/test")
    @Transactional
    public ResponseEntity<?> createComplexEventAutomatically() {
        Event event = new Event();
        event.setName("Metallica Concert 2");
        event.setDescription("An awesome Metallica concert");
        event.setEventDateStart(LocalDateTime.now().plusDays(10));
        event.setEventDateEnd(LocalDateTime.now().plusDays(10).plusHours(4));
        event.setMaxReservationsPerUser(5);
        event.setStatus(eventStatusRepository.findById(1L).get());
        event.setEventOwner(eventOwnerRepository.findById(1L).get());


        EventSeries eventSeries = new EventSeries();
        eventSeries.setName("Metallica World Tour 2024");
        eventSeries.setDescription("A festival of awesome music generated automatically");
        eventSeries.setEventSeriesStatus(eventStatusRepository.findById(1L).get());
        eventSeries.setEventOwner(eventOwnerRepository.findById(1L).get());
        eventSeriesRepository.save(eventSeries);

        Location location = new Location();
        location.setName("Polish National Stadium - PGE Narodowy");
        location.setCity("Warsaw");
        location.setCountry("Poland");
        location.setPostalCode("33-999");
        locationRepository.save(location);


        LocationVariant locationVariant = new LocationVariant();
        locationVariant.setName("Main concert variant");
        locationVariant.setDescription("A variant of the stadium for concerts");
        locationVariant.setLocation(location);
        locationVariantRepository.save(locationVariant);

        List<Sector> sectors = new ArrayList<>();


        for (int i = 1; i <= 2; i++) {
            Sector sector = new Sector();
            sector.setName("Sector " + i);
            sector.setStanding(false);
            sector.setLocationVariant(locationVariant);

            List<Row> rows = new ArrayList<>();

            for (int rowNum = 1; rowNum <= 5; rowNum++) {
                Row row = new Row();
                row.setRowNumber(rowNum);
                row.setSector(sector);

                List<Seat> seats = new ArrayList<>();

                for (int seatNum = 1; seatNum <= 10; seatNum++) {
                    Seat seat = new Seat();
                    seat.setSeatNumber(seatNum);
                    seat.setPositionX(seatNum * 5);
                    seat.setPositionY(rowNum * 10);
                    seat.setPositionRotation(0);
                    seat.setSeatForDisabled(seatNum % 5 == 0);
                    seat.setRow(row);
                    seats.add(seat);
                }

                row.setSeats(seats);
                rows.add(row);
            }

            sector.setRows(rows);
            sectorRepository.save(sector);
            sectors.add(sector);
        }

        Sector sec = new Sector();
        sec.setName("Sector standing 1");
        sec.setStanding(true);
        sec.setStandingCapacity(100);
        sec.setLocationVariant(locationVariant);
        sectorRepository.save(sec);
        sectors.add(sec);

        event.setEventSeries(eventSeries);
        event.setLocation(location);
        event.setLocationVariant(locationVariant);

        Event createdEvent = eventRepository.save(event);

        int count = 1;
        for (Sector sector : sectors) {
            TicketPrice normalPrice = new TicketPrice();
            normalPrice.setSector(sector);
            normalPrice.setEvent(event);
            normalPrice.setTicketType(ticketTypeRepository.findByName("Normal"));
            normalPrice.setPrice(50.00 * count);
            ticketPriceRepository.save(normalPrice);

            TicketPrice discountedPrice = new TicketPrice();
            discountedPrice.setSector(sector);
            discountedPrice.setEvent(event);
            discountedPrice.setTicketType(ticketTypeRepository.findByName("Discounted"));
            discountedPrice.setPrice(30.00 * count);
            ticketPriceRepository.save(discountedPrice);

            count++;
        }

        return new ResponseEntity<>(createdEvent, HttpStatus.CREATED);
    }
}
