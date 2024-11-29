package com.majlo.antares.controller;

import com.majlo.antares.config.UserAuthenticationProvider;
import com.majlo.antares.dtos.UserDto;
import com.majlo.antares.dtos.admin.EventOwnerDetailDto;
import com.majlo.antares.dtos.admin.EventOwnerPreviewDto;
import com.majlo.antares.dtos.admin.EventOwnerPreviewResponseDto;
import com.majlo.antares.dtos.creation.EventCreationDto;
import com.majlo.antares.dtos.eventDetail.EventDetailDto;
import com.majlo.antares.dtos.events.EventDto;
import com.majlo.antares.dtos.eventsListPreview.EventListPreviewDto;
import com.majlo.antares.model.EventOwner;
import com.majlo.antares.model.User;
import com.majlo.antares.model.events.Event;
import com.majlo.antares.model.events.EventSeries;
import com.majlo.antares.model.events.EventStatus;
import com.majlo.antares.model.location.*;
import com.majlo.antares.model.responses.EventResponseDto;
import com.majlo.antares.repository.EventOwnerRepository;
import com.majlo.antares.repository.UserRepository;
import com.majlo.antares.repository.events.EventRepository;
import com.majlo.antares.repository.events.EventSeriesRepository;
import com.majlo.antares.repository.events.EventStatusRepository;
import com.majlo.antares.repository.location.*;
import com.majlo.antares.service.AuthorizationService;
import com.majlo.antares.service.reservation.EventSeatStatusService;
import com.majlo.antares.specifications.EventResponseSpecification;
import jakarta.transaction.Status;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

@RestController
@RequestMapping("/api/events")
public class EventController {
    private final EventSeriesRepository eventSeriesRepository;
    private final EventRepository eventRepository;
    private final EventSeatStatusService eventSeatStatusService;
    private final LocationRepository locationRepository;
    private final LocationVariantRepository locationVariantRepository;
    private final SectorRepository sectorRepository;
    private final TicketTypeRepository ticketTypeRepository;
    private final TicketPriceRepository ticketPriceRepository;
    private final UserAuthenticationProvider userAuthenticationProvider;
    private final UserRepository userRepository;
    private final EventOwnerRepository eventOwnerRepository;
    private final AuthorizationService authorizationService;
    private final CityRepository cityRepository;
    private final EventStatusRepository eventStatusRepository;

    public EventController(EventSeriesRepository eventSeriesRepository, EventRepository eventRepository, EventSeatStatusService eventSeatStatusService, LocationRepository locationRepository, LocationVariantRepository locationVariantRepository, SectorRepository sectorRepository, TicketTypeRepository ticketTypeRepository, TicketPriceRepository ticketPriceRepository, UserAuthenticationProvider userAuthenticationProvider, UserRepository userRepository, EventOwnerRepository eventOwnerRepository, AuthorizationService authorizationService, CityRepository cityRepository, EventStatusRepository eventStatusRepository) {
        this.eventSeriesRepository = eventSeriesRepository;
        this.eventRepository = eventRepository;
        this.eventSeatStatusService = eventSeatStatusService;
        this.locationRepository = locationRepository;
        this.locationVariantRepository = locationVariantRepository;
        this.sectorRepository = sectorRepository;
        this.ticketTypeRepository = ticketTypeRepository;
        this.ticketPriceRepository = ticketPriceRepository;
        this.userAuthenticationProvider = userAuthenticationProvider;
        this.userRepository = userRepository;
        this.eventOwnerRepository = eventOwnerRepository;
        this.authorizationService = authorizationService;
        this.cityRepository = cityRepository;
        this.eventStatusRepository = eventStatusRepository;
    }


    @GetMapping
    public ResponseEntity<?> getEvents(
            @RequestParam(required = false) String cityName,
            @RequestParam(required = false) LocalDateTime dateStart,
            @RequestParam(required = false) LocalDateTime dateEnd,
            @RequestParam(required = false) String category,
            @RequestParam(required = false) String searchText,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "30") int size
    ) {
        try {
            if (page < 1) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Page number must be greater than or equal to 1");
            }

            Pageable pageable = PageRequest.of(page - 1, size);

            City city = cityRepository.findByCityName(cityName);

            Specification<Event> specification = EventResponseSpecification.filterByCriteria(city, dateStart, dateEnd, category, searchText);
            Page<Event> eventPage = eventRepository.findAll(specification, pageable);

            List<EventListPreviewDto> eventDtos = eventPage.stream().map(EventListPreviewDto::fromEvent).toList();

            EventResponseDto eventResponseDto = EventResponseDto.builder()
                    .events(eventDtos)
                    .totalPages(eventPage.getTotalPages())
                    .totalElements(eventPage.getTotalElements())
                    .pageNo(page)
                    .build();

            return ResponseEntity.ok(eventResponseDto);
        }
        catch (Exception e) {
            return ResponseEntity.badRequest().body("Error while fetching events: " + e.getMessage());
        }
    }

    @GetMapping("/search")
    public EventResponseDto getEvents(
            @RequestParam(required = false) String searchText,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "30") int size
    ) {
        if (page < 1) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Page number must be greater than or equal to 1");
        }

        Pageable pageable = PageRequest.of(page - 1, size);

        Specification<Event> specification = EventResponseSpecification.filterByEventName(searchText);
        Page<Event> eventPage = eventRepository.findAll(specification, pageable);

        List<EventListPreviewDto> eventDtos = eventPage.stream().map(EventListPreviewDto::fromEvent).toList();

        return EventResponseDto.builder()
                .events(eventDtos)
                .totalPages(eventPage.getTotalPages())
                .totalElements(eventPage.getTotalElements())
                .pageNo(page)
                .build();
    }

    @PostMapping("/create")
    @Transactional
    public ResponseEntity<?> createEvent(
            @RequestHeader("Authorization") String authHeader,
            @RequestBody EventCreationDto eventcreationDto) {

        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            Long userId = authorizationService.getAuthenticatedUserId(authHeader);
            EventOwner eventOwner = eventOwnerRepository.findById(userId).orElseThrow();

            if (eventOwner == null) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("User is not an event owner");
            }


            Event event = eventcreationDto.toEvent();
            event.setCreatedAt(LocalDateTime.now());
            event.setEventOwner(eventOwner);
            Event createdEvent = eventRepository.save(event);

            return ResponseEntity.status(HttpStatus.CREATED).body(EventDto.fromEvent(createdEvent));
        }

        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Authorization token is missing or invalid");
    }

    @PostMapping("/edit/{eventId}")
    @Transactional
    public ResponseEntity<?> editEvent(
            @RequestHeader("Authorization") String authHeader,
            @PathVariable Long eventId,
            @RequestBody EventCreationDto eventcreationDto) {

        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            Long userId = authorizationService.getAuthenticatedUserId(authHeader);
            EventOwner eventOwner = eventOwnerRepository.findById(userId).orElseThrow();

            if (eventOwner == null) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("User is not an event owner");
            }

            Event event = eventRepository.findById(eventId).orElseThrow();

            if (eventOwner.getEvents().contains(event)) {
                event = eventcreationDto.toEvent();
                event.setUpdatedAt(LocalDateTime.now());
                Event createdEvent = eventRepository.save(event);

                return ResponseEntity.status(HttpStatus.CREATED).body("Event updated successfully");
            }

            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("User is not the owner of the event");
        }

        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Authorization token is missing or invalid");
    }

    @GetMapping("/event/{id}")
    public EventDetailDto getEvent(@PathVariable Long id) {
        return eventRepository.findById(id)
                .map(EventDetailDto::fromEvent)
                .orElseThrow();
    }

    @GetMapping("/owner_detail/{id}")
    public EventOwnerDetailDto getEventDetailOwner(@PathVariable Long id) {
        return eventRepository.findById(id)
                .map(EventOwnerDetailDto::fromEvent)
                .orElseThrow();
    }


    @GetMapping("/owner_preview")
    public ResponseEntity<?> getEventsOwner(
//            @RequestHeader("Authorization") String authHeader,
            @RequestParam(required = false) String cityName,
            @RequestParam(required = false) LocalDateTime dateStart,
            @RequestParam(required = false) LocalDateTime dateEnd,
            @RequestParam(required = false) String category,
            @RequestParam(required = false) String status,
            @RequestParam(required = false) Boolean isPublic,
            @RequestParam(required = false) String searchText,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "50") int size
    ) {
//        if (authHeader != null && authHeader.startsWith("Bearer ")) {
//            Long userId = authorizationService.getAuthenticatedUserId(authHeader);
            EventOwner eventOwner = eventOwnerRepository.findById(1L).orElseThrow();

            if (eventOwner == null) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("User is not an event owner");
            }

            if (page < 1) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Page number must be greater than or equal to 1");
            }

            Pageable pageable = PageRequest.of(page - 1, size);

            City city = cityRepository.findByCityName(cityName);

            Specification<Event> specification = EventResponseSpecification.filterByCriteria(city, dateStart, dateEnd, category, status, isPublic, searchText);
            Page<Event> eventPage = eventRepository.findAll(specification, pageable);

            List<EventOwnerPreviewDto> eventPreviewDtos = eventPage.stream().map(EventOwnerPreviewDto::fromEvent).toList();

            EventOwnerPreviewResponseDto eventResponseDto = EventOwnerPreviewResponseDto.builder()
                    .events(eventPreviewDtos)
                    .totalPages(eventPage.getTotalPages())
                    .totalElements(eventPage.getTotalElements())
                    .pageNo(page)
                    .build();

            return ResponseEntity.ok(eventResponseDto);
//        }
//
//        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Authorization token is missing or invalid");


    }









    // TODO: Additional function for setting ticket price for sector

    // TODO: Add JWT token, account and role validation
    @PostMapping("/generateEventSeatStatusEntities")
    @Transactional
    public ResponseEntity<?> generateEventSeatStatusEntities(@RequestParam Long event_id) {
        try {
            Event event = eventRepository.findById(event_id).orElseThrow(() ->
                    new NoSuchElementException("Event with ID " + event_id + " not found"));

            eventSeatStatusService.generateEventSeatStatuses(event);
            return ResponseEntity.ok("Event seat statuses generated successfully for event ID: " + event_id);

        } catch (NoSuchElementException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("An error occurred while generating seat statuses: " + e.getMessage());
        }
    }
}
