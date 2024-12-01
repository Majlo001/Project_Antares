package com.majlo.antares.controller;


import com.majlo.antares.dtos.admin.EventSeriesForEventOwnerDetailDto;
import com.majlo.antares.dtos.events.EventDto;
import com.majlo.antares.dtos.events.EventSeriesDto;
import com.majlo.antares.model.EventOwner;
import com.majlo.antares.model.events.Event;
import com.majlo.antares.model.events.EventCategory;
import com.majlo.antares.model.events.EventSeries;
import com.majlo.antares.repository.EventOwnerRepository;
import com.majlo.antares.repository.events.*;
import com.majlo.antares.service.AuthorizationService;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/eventseries")
public class EventSeriesController {
    private final EventSeriesRepository eventSeriesRepository;
    private final EventRepository eventRepository;
    private final AuthorizationService authorizationService;
    private final EventOwnerRepository eventOwnerRepository;
    private final EntityManager entityManager;
    private final ArtistRepostiory artistRepostiory;
    private final EventTagRepository eventTagRepository;
    private final EventStatusRepository eventStatusRepository;

    public EventSeriesController(EventSeriesRepository eventSeriesRepository, EventRepository eventRepository, AuthorizationService authorizationService, EventOwnerRepository eventOwnerRepository, EntityManager entityManager, ArtistRepostiory artistRepostiory, EventTagRepository eventTagRepository, EventStatusRepository eventStatusRepository) {
        this.eventSeriesRepository = eventSeriesRepository;
        this.eventRepository = eventRepository;
        this.authorizationService = authorizationService;
        this.eventOwnerRepository = eventOwnerRepository;
        this.entityManager = entityManager;
        this.artistRepostiory = artistRepostiory;
        this.eventTagRepository = eventTagRepository;
        this.eventStatusRepository = eventStatusRepository;
    }

    @GetMapping
    public List<EventSeriesDto> getEventSeriesList() {
        return eventSeriesRepository.findAll()
                .stream()
                .map(EventSeriesDto::fromEventSeries)
                .toList();
    }


    @GetMapping("/{id}")
    public EventSeriesDto getEventSeries(@PathVariable Long id) {
        EventSeries eventSeries = eventSeriesRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));

        return EventSeriesDto.fromEventSeries(eventSeries);
    }

    @PostMapping
    public EventSeriesDto createEventSeries(@RequestBody EventSeriesDto eventSeriesDto) {
        EventSeries eventSeries = eventSeriesDto.toEventSeries();
        EventSeries savedEventSeries = eventSeriesRepository.save(eventSeries);
        return EventSeriesDto.fromEventSeries(savedEventSeries);
    }

    @PutMapping("/{id}")
    public EventSeriesDto updateEventSeries(@PathVariable Long id, @RequestBody EventSeriesDto eventSeriesDto) {
        EventSeries eventSeries = eventSeriesRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        eventSeries.setId(id);
        EventSeries savedEventSeries = eventSeriesRepository.save(eventSeries);
        return EventSeriesDto.fromEventSeries(savedEventSeries);
    }

    @PutMapping("/{id}/add_event")
    public EventSeriesDto addEventToEventSeries(@PathVariable Long id, @RequestBody Event event) {
        EventSeries eventSeries = eventSeriesRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        event.setEventSeries(eventSeries);
        Event savedEvent = eventRepository.save(event);
        eventSeries.getEvents().add(savedEvent);
        EventSeries savedEventSeries = eventSeriesRepository.save(eventSeries);
        return EventSeriesDto.fromEventSeries(savedEventSeries);
    }

    @DeleteMapping("/{id}")
    public void deleteEventSeries(@PathVariable Long id) {
        EventSeries eventSeries = eventSeriesRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        eventSeriesRepository.delete(eventSeries);
    }




    @GetMapping("/single_event_series/{eventSeriesId}")
    public ResponseEntity<?> getSingleEventSeries(
            @RequestHeader("Authorization") String authHeader,
            @PathVariable Long eventSeriesId) {

        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            Long userId = authorizationService.getAuthenticatedUserId(authHeader);
            EventOwner eventOwner = eventOwnerRepository.findById(userId).orElse(null);

            if (eventOwner == null) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("User is not an event owner");
            }


            EventSeries eventSeries = eventSeriesRepository.findById(eventSeriesId).orElse(null);

            if (eventSeries == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Event series not found");
            }

            if (!eventSeries.getEventOwner().getId().equals(userId)) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("User is not an owner of this event series");
            }

            if (!eventSeries.getIsSingleEvent()) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Event series is not a single event");
            }

            return ResponseEntity.status(HttpStatus.OK).body(EventSeriesForEventOwnerDetailDto.fromEventSeries(eventSeries));
        }

        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("User is not authenticated");
    }


    @PostMapping("/create_single")
    @Transactional
    public ResponseEntity<?> createSingleEventSeries(
            @RequestHeader("Authorization") String authHeader,
            @RequestBody EventSeriesForEventOwnerDetailDto eventSeriesForEventOwnerDetailDto) {

        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            Long userId = authorizationService.getAuthenticatedUserId(authHeader);
            EventOwner eventOwner = eventOwnerRepository.findById(userId).orElse(null);

            if (eventOwner == null) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("User is not an event owner");
            }


            EventSeries eventSeries = eventSeriesForEventOwnerDetailDto.toEventSeries(eventTagRepository, artistRepostiory);
            eventSeries.setIsSingleEvent(true);
            eventSeries.setEventOwner(eventOwner);

            //TODO: set event series status
            eventSeries.setEventSeriesStatus(eventStatusRepository.getReferenceById(1L));
            EventSeries savedEventSeries = eventSeriesRepository.save(eventSeries);


            Map<String, Long> response = Map.of("id", savedEventSeries.getId());
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        }

        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("User is not authenticated");
    }


    @PostMapping("/edit_single/{eventSeriesId}")
    @Transactional
    public ResponseEntity<?> editSingleEventSeries(
            @RequestHeader("Authorization") String authHeader,
            @PathVariable Long eventSeriesId,
            @RequestBody EventSeriesForEventOwnerDetailDto eventSeriesForEventOwnerDetailDto) {

        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            Long userId = authorizationService.getAuthenticatedUserId(authHeader);
            EventOwner eventOwner = eventOwnerRepository.findById(userId).orElse(null);

            if (eventOwner == null) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("User is not an event owner");
            }

            EventSeries eventSeries = eventSeriesRepository.findById(eventSeriesId).orElse(null);
            if (eventSeries == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Event series not found");
            }

            if (!eventSeries.getEventOwner().getId().equals(userId)) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("User is not an owner of this event series");
            }

            if (!eventSeries.getIsSingleEvent()) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Event series is not a single event");
            }

            eventSeries.setId(eventSeriesId);
            eventSeries.setDescription(eventSeriesForEventOwnerDetailDto.getDescription());
            eventSeries.setShortDescription(eventSeriesForEventOwnerDetailDto.getShortDescription());
            eventSeries.setName(eventSeriesForEventOwnerDetailDto.getName());
            eventSeries.setCategory(eventSeriesForEventOwnerDetailDto.getEventCategoryId() != null ? EventCategory.builder().eventCategoryId(eventSeriesForEventOwnerDetailDto.getEventCategoryId()).build() : null);
            eventSeries.setYoutubePreviewUrl(eventSeriesForEventOwnerDetailDto.getYoutubePreviewUrl());
            eventSeries.setEventTagsFromStrings(new HashSet<>(eventSeriesForEventOwnerDetailDto.getEventTags()), eventTagRepository);
            eventSeries.setArtistsFromIds(new HashSet<>(eventSeriesForEventOwnerDetailDto.getArtistsIds()), artistRepostiory);


            eventSeries.setIsSingleEvent(true);
            eventSeries.setEventOwner(eventOwner);
            eventSeriesRepository.save(eventSeries);

            return ResponseEntity.status(HttpStatus.OK).body("Event series updated");
        }

        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("User is not authenticated");
    }
}
