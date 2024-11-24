package com.majlo.antares.controller;

import com.majlo.antares.converter.CHPointsListConverter;
import com.majlo.antares.model.Artist;
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
import java.util.Set;

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
    private final ArtistRepostiory artistRepostiory;


    public CreateController(EventStatusRepository eventStatusRepository, LocationRepository locationRepository, LocationVariantRepository locationVariantRepository, SectorRepository sectorRepository, TicketPriceRepository ticketPriceRepository, EventSeriesRepository eventSeriesRepository, EventRepository eventRepository, TicketTypeRepository ticketTypeRepository, EventOwnerRepository eventOwnerRepository, CityRepository cityRepository, EventCategoryRepository eventCategoryRepository, EventTagRepository eventTagRepository, ArtistRepostiory artistRepostiory) {
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
        this.artistRepostiory = artistRepostiory;
    }

    @PostMapping("/all")
    public void createAll() {
        createStatuses();
        createCities();
        createEventCategories();
        createEventTags();
        createTicketTypes();
        createArtists();
        createEventLocations();
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

    @PostMapping("/event_artists")
    public void createArtists() {
        Artist artist1 = new Artist();
        artist1.setName("Metallica");
        artist1.setMainImage("/api/images/files/9f2e7251-897a-411d-ad88-01da01505f69.jpg");
        artist1.setDescription("Metallica is an American heavy metal band. The band was formed in 1981 in Los Angeles by vocalist/guitarist James Hetfield and drummer Lars Ulrich, and has been based in San Francisco for most of its career. The band's fast tempos, instrumentals and aggressive musicianship made them one of the founding 'big four' bands of thrash metal, alongside Megadeth, Anthrax and Slayer. Metallica's current lineup comprises founding members and primary songwriters Hetfield and Ulrich, longtime lead guitarist Kirk Hammett, and bassist Robert Trujillo. Guitarist Dave Mustaine (who formed Megadeth) and bassists Ron McGovney, Cliff Burton and Jason Newsted are former members of the band.");
        artist1.setWebsiteUrl("https://www.metallica.com/");
        artistRepostiory.save(artist1);

        Artist artist2 = new Artist();
        artist2.setName("Arctic Monkeys");
        artist2.setMainImage("/api/images/files/ebd00d6d-3346-4125-b641-267990d572f5.jpg");
        artist2.setDescription("Arctic Monkeys are an English rock band formed in Sheffield in 2002. The group consists of Alex Turner (lead vocals, guitar, keyboards), Jamie Cook (guitar, keyboards), Nick O'Malley (bass guitar, backing vocals), and Matt Helders (drums, backing vocals). Former band member Andy Nicholson (bass guitar, backing vocals) left the band in 2006 shortly after their debut album was released.");
        artist2.setWebsiteUrl("https://www.arcticmonkeys.com/");
        artistRepostiory.save(artist2);

        Artist artist3 = new Artist();
        artist3.setName("AC/DC");
        artist3.setMainImage("/api/images/files/79e6ec4c-df40-457e-bcc4-b6c21de8bdec.jpg");
        artist3.setDescription("AC/DC are an Australian rock band formed in Sydney in 1973 by Scottish-born brothers Malcolm and Angus Young. Although their music has been variously described as hard rock, blues rock, and heavy metal, the band themselves call it simply 'rock and roll'. AC/DC underwent several line-up changes before releasing their first album, High Voltage, in 1975.");
        artist3.setWebsiteUrl("https://www.acdc.com/");
        artistRepostiory.save(artist3);
    }


    @PostMapping("/event_locations")
    @Transactional
    public void createEventLocations() {
        Location location = new Location();
        location.setName("Polish National Stadium - PGE Narodowy");
        location.setMainImage("/api/images/files/e4774d6d-3346-kf25-b641-26984760d572f5.jpg");

        location.setCity(cityRepository.findById(1L).get());
        location.setAddress("Aleja Księcia Józefa Poniatowskiego 1");
        location.setPostalCode("03-901");
        location.setWebsite("https://www.pgenarodowy.pl/");
        location.setGoogleMapsLink("https://maps.app.goo.gl/x5vuSkkWNmmF31bNA");
        locationRepository.save(location);

        LocationVariant locationVariant = new LocationVariant();
        locationVariant.setName("Main concert variant");
        locationVariant.setDescription("A variant of the stadium for concerts");
        locationVariant.setLocation(location);
        locationVariantRepository.save(locationVariant);

        List<Sector> sectors = new ArrayList<>();


        for (int i = 1; i <= 3; i++) {
            Sector sector = new Sector();
            sector.setName("Sector " + i);
            sector.setStanding(false);
            sector.setLocationVariant(locationVariant);
            sector.setPositionX(240 * i);
            sector.setPositionY(0);

            List<Sector.CHPoints> points = new ArrayList<>();
            points.add(new Sector.CHPoints(0, 0));
            points.add(new Sector.CHPoints(0, 200));
            points.add(new Sector.CHPoints(200, 200));
            points.add(new Sector.CHPoints(200, 0));
            sector.setConvexHullPoints(points);

            List<Row> rows = new ArrayList<>();

            for (int rowNum = 1; rowNum <= 20; rowNum++) {
                Row row = new Row();
                row.setRowNumber(rowNum);
                row.setSector(sector);
                row.setPositionX(0);
                row.setPositionY(rowNum * -15);
                row.setPositionRotation(90);

                List<Seat> seats = new ArrayList<>();

                for (int seatNum = 1; seatNum <= 14; seatNum++) {
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


        Location location2 = new Location();
        location2.setName("Netto Arena");
        location2.setMainImage("/api/images/files/91db19bc-af49-4c4f-8b34-cfaf9d444331.jpg");

        location2.setCity(cityRepository.findById(7L).get());
        location2.setAddress("Władysława Szafera 3/5/7");
        location2.setPostalCode("71-245");
        location2.setWebsite("https://netto.arenaszczecin.eu/");
        location2.setGoogleMapsLink("https://maps.app.goo.gl/TgCDaJRxu6XzyN3S9");
        locationRepository.save(location2);

        LocationVariant locationVariant2 = new LocationVariant();
        locationVariant2.setName("Main concert variant of Netto Arena");
        locationVariant2.setDescription("A variant of the arena for concerts");
        locationVariant2.setLocation(location2);
        locationVariantRepository.save(locationVariant2);


        List<Sector> sectors2 = new ArrayList<>();
        for (int i = 1; i <= 2; i++) {
            Sector sector = new Sector();
            sector.setName("Sector " + i);
            sector.setStanding(false);
            sector.setLocationVariant(locationVariant2);
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
            sectors2.add(sector);
        }

        Sector sec2 = new Sector();
        sec2.setName("Sector standing 1");
        sec2.setStanding(true);
        sec2.setStandingCapacity(100);
        sec2.setLocationVariant(locationVariant2);
        sectorRepository.save(sec2);
        sectors2.add(sec2);
    }


    @PostMapping("/test1")
    @Transactional
    public ResponseEntity<?> createComplexEventAutomatically() {
        try {
            Event event = new Event();
            event.setName("Metallica Concert");
            event.setDescription("An awesome Metallica concert");
            event.setShortDescription("An awesome Metallica concert");
            event.setEventDateStart(LocalDateTime.now().plusDays(10));
            event.setEventDateEnd(LocalDateTime.now().plusDays(10).plusHours(4));
            event.setMaxReservationsPerUser(5);
            event.setStatus(eventStatusRepository.findById(1L).get());
            event.setEventOwner(eventOwnerRepository.findById(1L).get());
            event.setCreatedAt(LocalDateTime.now());
            event.setMainImage("/api/images/files/b9fec7e0-2de4-4d6e-8f7b-761df7d2efcb.jpg");


            EventSeries eventSeries = new EventSeries();
            eventSeries.setName("Metallica World Tour 2024");
            eventSeries.setDescription("A festival of awesome music generated automatically");
            eventSeries.setEventSeriesStatus(eventStatusRepository.findById(1L).get());
            eventSeries.setEventOwner(eventOwnerRepository.findById(1L).get());
            eventSeries.setCategory(eventCategoryRepository.findById(1L).get());

            Set<Artist> artists = Set.of(artistRepostiory.findById(1L).get());
            eventSeries.setArtists(artists);
            eventSeries.setIsSingleEvent(false);
            eventSeriesRepository.save(eventSeries);


            event.setEventSeries(eventSeries);
            event.setLocation(locationRepository.findById(1L).orElseThrow(() -> new RuntimeException("Location not found")));
            event.setLocationVariant(locationVariantRepository.findById(1L).orElseThrow(() -> new RuntimeException("Location variant not found")));
            eventRepository.save(event);

            List<Sector> sectors = sectorRepository.findAllByLocationVariant(locationVariantRepository.findById(1L).get());

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

            return new ResponseEntity<>(HttpStatus.CREATED);
        }
        catch (Exception e) {
            System.out.println(e.getMessage());
            e.printStackTrace();
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }


    }

    @PostMapping("/test2")
    @Transactional
    public ResponseEntity<?> createComplexEventAutomatically2() {
        Event event = new Event();
        event.setName("AC/DC Concert");
        event.setDescription("An awesome AC/DC concert");
        event.setShortDescription("An awesome AC/DC concert");
        event.setEventDateStart(LocalDateTime.now().plusDays(10));
        event.setEventDateEnd(LocalDateTime.now().plusDays(10).plusHours(4));
        event.setMaxReservationsPerUser(5);
        event.setStatus(eventStatusRepository.findById(1L).get());
        event.setEventOwner(eventOwnerRepository.findById(1L).get());
        event.setCreatedAt(LocalDateTime.now());
        event.setMainImage("/api/images/files/33311393-5834-408a-b079-b214c320508b.jpg");


        EventSeries eventSeries = new EventSeries();
        eventSeries.setName("AC/DC World Tour 2024");
        eventSeries.setDescription("A festival of awesome music generated automatically");
        eventSeries.setEventSeriesStatus(eventStatusRepository.findById(1L).get());
        eventSeries.setEventOwner(eventOwnerRepository.findById(1L).get());
        eventSeries.setCategory(eventCategoryRepository.findById(1L).get());

        Set<Artist> artists = Set.of(artistRepostiory.findById(3L).get());
        eventSeries.setArtists(artists);
        eventSeries.setIsSingleEvent(false);
        eventSeriesRepository.save(eventSeries);


        event.setEventSeries(eventSeries);
        event.setLocation(locationRepository.findById(2L).orElseThrow(() -> new RuntimeException("Location not found")));
        event.setLocationVariant(locationVariantRepository.findById(2L).orElseThrow(() -> new RuntimeException("Location Variant not found")));
        eventRepository.save(event);

        List<Sector> sectors = sectorRepository.findAllByLocationVariant(locationVariantRepository.findById(2L).orElseThrow(() -> new RuntimeException("Location Variant not found")));

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



        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @PostMapping("/test3")
    @Transactional
    public ResponseEntity<?> createComplexEventAutomatically3() {
        Event event = new Event();
        event.setName("Arctic Monkeys Concert");
        event.setDescription("An awesome Arctic Monkeys concert");
        event.setShortDescription("An awesome Arctic Monkeys concert");
        event.setEventDateStart(LocalDateTime.now().plusDays(10));
        event.setEventDateEnd(LocalDateTime.now().plusDays(10).plusHours(4));
        event.setMaxReservationsPerUser(5);
        event.setStatus(eventStatusRepository.findById(1L).get());
        event.setEventOwner(eventOwnerRepository.findById(1L).get());
        event.setCreatedAt(LocalDateTime.now());
        event.setMainImage("/api/images/files/469a6096-203d-48d0-8752-872ed80daefc.jpg");

        EventSeries eventSeries = new EventSeries();
        eventSeries.setName("Arctic Monkeys World Tour 2024");
        eventSeries.setDescription("A festival of awesome music generated automatically");
        eventSeries.setEventSeriesStatus(eventStatusRepository.findById(1L).get());
        eventSeries.setEventOwner(eventOwnerRepository.findById(1L).get());
        eventSeries.setCategory(eventCategoryRepository.findById(1L).get());

        Set<Artist> artists = Set.of(artistRepostiory.findById(2L).get());
        eventSeries.setArtists(artists);
        eventSeries.setIsSingleEvent(false);
        eventSeriesRepository.save(eventSeries);


        event.setEventSeries(eventSeries);
        event.setLocation(locationRepository.findById(1L).orElseThrow(() -> new RuntimeException("Location not found")));
        event.setLocationVariant(locationVariantRepository.findById(1L).orElseThrow(() -> new RuntimeException("Location Variant not found")));
        eventRepository.save(event);

        List<Sector> sectors = sectorRepository.findAllByLocationVariant(locationVariantRepository.findById(1L).orElseThrow(() -> new RuntimeException("Location Variant not found")));

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


        return new ResponseEntity<>(HttpStatus.CREATED);
    }
}
