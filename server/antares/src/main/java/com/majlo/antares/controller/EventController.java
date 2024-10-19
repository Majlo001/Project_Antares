package com.majlo.antares.controller;

import com.majlo.antares.dtos.events.EventDto;
import com.majlo.antares.model.events.Event;
import com.majlo.antares.model.events.EventSeries;
import com.majlo.antares.model.location.*;
import com.majlo.antares.model.reservation.EventSeatStatus;
import com.majlo.antares.model.responses.EventResponseDto;
import com.majlo.antares.repository.events.EventRepository;
import com.majlo.antares.repository.events.EventSeriesRepository;
import com.majlo.antares.repository.location.*;
import com.majlo.antares.repository.reservation.EventSeatStatusRepository;
import com.majlo.antares.specifications.EventResponseSpecification;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("/api/events")
public class EventController {
    private final EventSeriesRepository eventSeriesRepository;
    private final EventRepository eventRepository;

    public EventController(EventSeriesRepository eventSeriesRepository, EventRepository eventRepository) {
        this.eventSeriesRepository = eventSeriesRepository;
        this.eventRepository = eventRepository;
    }

    @Autowired
    private LocationRepository locationRepository;

    @Autowired
    private LocationVariantRepository locationVariantRepository;

    @Autowired
    private SectorRepository sectorRepository;

    @Autowired
    private RowRepository rowRepository;

    @Autowired
    private SeatRepository seatRepository;

    @Autowired
    private EventSeatStatusRepository seatStatusRepository;

//    @GetMapping
//    public List<EventDto> getAllEvents() {
//        return eventRepository.findAll()
//                .stream()
//                .map(EventDto::fromEvent)
//                .toList();
//    }


    @GetMapping
    public EventResponseDto getEvents(
            @RequestParam(required = false) Location location,
            @RequestParam(required = false) LocalDateTime dateStart,
            @RequestParam(required = false) LocalDateTime dateEnd,
            @RequestParam(required = false) String tag,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "30") int size
    ) {
        if (page < 1) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Page number must be greater than or equal to 1");
        }

        Pageable pageable = PageRequest.of(page - 1, size);

        Specification<Event> specification = EventResponseSpecification.filterByCriteria(location, dateStart, dateEnd, tag);
        Page<Event> eventPage = eventRepository.findAll(specification, pageable);

        List<EventDto> eventDtos = eventPage.stream().map(EventDto::fromEvent).toList();

        return EventResponseDto.builder()
                .events(eventDtos)
                .pageNo(eventPage.getNumber())
                .pageSize(eventPage.getSize())
                .totalElements(eventPage.getTotalElements())
                .totalPages(eventPage.getTotalPages())
                .build();
    }

    @GetMapping("/newwwww/{id}")
    public EventDto getEvent(@PathVariable Long id) {
        return eventRepository.findById(id)
                .map(EventDto::fromEvent)
                .orElseThrow();
    }



    @PostMapping("/test")
    @Transactional
    public ResponseEntity<Event> createComplexEventAutomatically() {
        Event event = new Event();
        event.setName("Automatic Rock Concert");
        event.setDescription("An awesome rock concert created automatically");
        event.setEventDateStart(LocalDateTime.now().plusDays(10));
        event.setEventDateEnd(LocalDateTime.now().plusDays(10).plusHours(4));
        event.setMaxReservationsPerUser(5);

        EventSeries eventSeries = new EventSeries();
        eventSeries.setName("Auto Music Festival");
        eventSeries.setDescription("A festival of awesome music generated automatically");
        eventSeriesRepository.save(eventSeries);

        Location location = new Location();
        location.setName("Auto Stadium");
        location.setCity("Auto City");
        location.setCountry("Auto Country");
        location.setPostalCode("12345");
        locationRepository.save(location);


        LocationVariant locationVariant = new LocationVariant();
        locationVariant.setName("Main Football Stadium Variant");
        locationVariant.setDescription("A variant of the stadium for large events");
        locationVariant.setLocation(location);
        locationVariantRepository.save(locationVariant);

        List<Sector> sectors = new ArrayList<>();


        for (int i = 1; i <= 2; i++) {
            Sector sector = new Sector();
            sector.setName("Sector " + i);
            sector.setType("Sitting");
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

        event.setEventSeries(eventSeries);
        event.setLocation(location);
        event.setLocationVariant(locationVariant);

        Event createdEvent = eventRepository.save(event);


        for (Sector sector : sectors) {
            for (Row row : sector.getRows()) {
                for (Seat seat : row.getSeats()) {
                    EventSeatStatus seatStatus = new EventSeatStatus();
                    seatStatus.setSeat(seat);
                    seatStatus.setEvent(createdEvent);
                    seatStatus.setSeatUnavailable(false);
                    seatStatus.setReservationTime(null);
                    seatStatus.setExpirationTime(null);
                    seatStatusRepository.save(seatStatus);
                }
            }
        }


        return new ResponseEntity<>(createdEvent, HttpStatus.CREATED);
    }
}
