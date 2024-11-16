package com.majlo.antares.controller;

import com.majlo.antares.converter.CHPointsListConverter;
import com.majlo.antares.model.events.*;
import com.majlo.antares.model.location.*;
import com.majlo.antares.repository.EventOwnerRepository;
import com.majlo.antares.repository.events.*;
import com.majlo.antares.repository.location.*;
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
    private final CityRepository cityRepository;
    private final EventCategoryRepository eventCategoryRepository;
    private final EventTagRepository eventTagRepository;


    public CreateController(EventStatusRepository eventStatusRepository, LocationRepository locationRepository, LocationVariantRepository locationVariantRepository, SectorRepository sectorRepository, TicketPriceRepository ticketPriceRepository, EventSeriesRepository eventSeriesRepository, EventRepository eventRepository, TicketTypeRepository ticketTypeRepository, EventOwnerRepository eventOwnerRepository, CityRepository cityRepository, EventCategoryRepository eventCategoryRepository, EventTagRepository eventTagRepository) {
        this.eventStatusRepository = eventStatusRepository;
        this.locationRepository = locationRepository;
        this.locationVariantRepository = locationVariantRepository;
        this.sectorRepository = sectorRepository;
        this.ticketPriceRepository = ticketPriceRepository;
        this.eventSeriesRepository = eventSeriesRepository;
        this.eventRepository = eventRepository;
        this.ticketTypeRepository = ticketTypeRepository;
        this.eventOwnerRepository = eventOwnerRepository;
        this.cityRepository = cityRepository;
        this.eventCategoryRepository = eventCategoryRepository;
        this.eventTagRepository = eventTagRepository;
    }

    @PostMapping("/statuses")
    public void createStatuses() {
        List<EventStatus> statuses = Arrays.asList(
                EventStatus.builder().eventStatusName("Planned").build(),
                EventStatus.builder().eventStatusName("Scheduled").build(),
                EventStatus.builder().eventStatusName("Sold out").build(),
                EventStatus.builder().eventStatusName("Finished").build(),
                EventStatus.builder().eventStatusName("Cancelled").build(),
                EventStatus.builder().eventStatusName("Postponed").build()
        );

        eventStatusRepository.saveAll(statuses);
    }

    @PostMapping("/cities")
    public void createCities() {
        List<City> cities = Arrays.asList(
                City.builder().cityName("Warsaw").country("Poland").build(),
                City.builder().cityName("Kraków").country("Poland").build(),
                City.builder().cityName("Gdańsk").country("Poland").build(),
                City.builder().cityName("Wrocław").country("Poland").build(),
                City.builder().cityName("Poznań").country("Poland").build(),
                City.builder().cityName("Łódź").country("Poland").build(),
                City.builder().cityName("Szczecin").country("Poland").build(),
                City.builder().cityName("Lublin").country("Poland").build(),
                City.builder().cityName("Katowice").country("Poland").build(),
                City.builder().cityName("Bialystok").country("Poland").build(),
                City.builder().cityName("New York").country("USA").build(),
                City.builder().cityName("Los Angeles").country("USA").build(),
                City.builder().cityName("Chicago").country("USA").build(),
                City.builder().cityName("Houston").country("USA").build(),
                City.builder().cityName("London").country("UK").build(),
                City.builder().cityName("Manchester").country("UK").build(),
                City.builder().cityName("Liverpool").country("UK").build(),
                City.builder().cityName("Birmingham").country("UK").build(),
                City.builder().cityName("Berlin").country("Germany").build(),
                City.builder().cityName("Munich").country("Germany").build(),
                City.builder().cityName("Hamburg").country("Germany").build(),
                City.builder().cityName("Frankfurt").country("Germany").build(),
                City.builder().cityName("Paris").country("France").build(),
                City.builder().cityName("Marseille").country("France").build(),
                City.builder().cityName("Lyon").country("France").build()
        );

        cityRepository.saveAll(cities);
    }

    @PostMapping("/event_categories")
    public void createEventCategories() {
        List<EventCategory> eventCategories = Arrays.asList(
                EventCategory.builder().eventCategoryName("Music").build(),
                EventCategory.builder().eventCategoryName("Sport").build(),
                EventCategory.builder().eventCategoryName("Theatre").build(),
                EventCategory.builder().eventCategoryName("Cinema").build(),
                EventCategory.builder().eventCategoryName("Festival").build(),
                EventCategory.builder().eventCategoryName("Conference").build(),
                EventCategory.builder().eventCategoryName("Exhibition").build(),
                EventCategory.builder().eventCategoryName("Other").build()
        );

        eventCategoryRepository.saveAll(eventCategories);
    }

    @PostMapping("/event_tags")
    public void createEventTags() {
        List<EventTag> eventTags = Arrays.asList(
                EventTag.builder().tagName("Metallica").build(),
                EventTag.builder().tagName("Rock").build(),
                EventTag.builder().tagName("Metal").build(),
                EventTag.builder().tagName("Jazz").build(),
                EventTag.builder().tagName("Pop").build(),
                EventTag.builder().tagName("Rap").build(),
                EventTag.builder().tagName("Hip-hop").build(),
                EventTag.builder().tagName("Classical").build(),
                EventTag.builder().tagName("Blues").build(),
                EventTag.builder().tagName("Football").build(),
                EventTag.builder().tagName("Basketball").build(),
                EventTag.builder().tagName("Volleyball").build(),
                EventTag.builder().tagName("Tennis").build(),
                EventTag.builder().tagName("Boxing").build(),
                EventTag.builder().tagName("MMA").build()
        );

        eventTagRepository.saveAll(eventTags);
    }

    @PostMapping("/event_ticket_types")
    public void createTicketTypes() {
        TicketType normalTicket = new TicketType();
        normalTicket.setName("Normal");
        ticketTypeRepository.save(normalTicket);

        TicketType discountedTicket = new TicketType();
        discountedTicket.setName("Discounted");
        ticketTypeRepository.save(discountedTicket);

        List<TicketType> ticketTypes = new ArrayList<>();
        ticketTypes.add(normalTicket);
        ticketTypes.add(discountedTicket);
    }



    @PostMapping("/test")
    @Transactional
    public ResponseEntity<?> createComplexEventAutomatically() {
        Event event = new Event();
        event.setName("Metallica Concert");
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
        eventSeries.setIsSingleEvent(true);
        eventSeriesRepository.save(eventSeries);

        Location location = new Location();
        location.setName("Polish National Stadium - PGE Narodowy");
        location.setCity(cityRepository.findById(1L).get());
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
            sector.setPositionX(200 * i);
            sector.setPositionY(0);

            List<Sector.CHPoints> points = new ArrayList<>();
            points.add(new Sector.CHPoints(0, 0));
            points.add(new Sector.CHPoints(0, 200));
            points.add(new Sector.CHPoints(200, 200));
            points.add(new Sector.CHPoints(200, 0));
            sector.setConvexHullPoints(points);

            List<Row> rows = new ArrayList<>();

            for (int rowNum = 1; rowNum <= 10; rowNum++) {
                Row row = new Row();
                row.setRowNumber(rowNum);
                row.setSector(sector);
                row.setPositionX(0);
                row.setPositionY(rowNum * -15);
                row.setPositionRotation(90);

                List<Seat> seats = new ArrayList<>();

                for (int seatNum = 1; seatNum <= 10; seatNum++) {
                    Seat seat = new Seat();
                    seat.setSeatNumber(seatNum);
                    seat.setPositionX(seatNum * 15);
                    seat.setPositionY(6);
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
