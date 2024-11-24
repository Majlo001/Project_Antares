package com.majlo.antares.controller;

import com.majlo.antares.config.UserAuthenticationProvider;
import com.majlo.antares.dtos.UserDto;
import com.majlo.antares.dtos.creation.EventCreationDto;
import com.majlo.antares.dtos.eventDetail.EventDetailDto;
import com.majlo.antares.dtos.events.EventDto;
import com.majlo.antares.dtos.eventsListPreview.EventListPreviewDto;
import com.majlo.antares.model.EventOwner;
import com.majlo.antares.model.User;
import com.majlo.antares.model.events.Event;
import com.majlo.antares.model.events.EventSeries;
import com.majlo.antares.model.location.*;
import com.majlo.antares.model.responses.EventResponseDto;
import com.majlo.antares.repository.EventOwnerRepository;
import com.majlo.antares.repository.UserRepository;
import com.majlo.antares.repository.events.EventRepository;
import com.majlo.antares.repository.events.EventSeriesRepository;
import com.majlo.antares.repository.location.*;
import com.majlo.antares.service.AuthorizationService;
import com.majlo.antares.service.reservation.EventSeatStatusService;
import com.majlo.antares.specifications.EventResponseSpecification;
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

    public EventController(EventSeriesRepository eventSeriesRepository, EventRepository eventRepository, EventSeatStatusService eventSeatStatusService, LocationRepository locationRepository, LocationVariantRepository locationVariantRepository, SectorRepository sectorRepository, TicketTypeRepository ticketTypeRepository, TicketPriceRepository ticketPriceRepository, UserAuthenticationProvider userAuthenticationProvider, UserRepository userRepository, EventOwnerRepository eventOwnerRepository, AuthorizationService authorizationService) {
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
    }

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

    @GetMapping("/event/{id}")
    public EventDetailDto getEvent(@PathVariable Long id) {
        return eventRepository.findById(id)
                .map(EventDetailDto::fromEvent)
                .orElseThrow();
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
