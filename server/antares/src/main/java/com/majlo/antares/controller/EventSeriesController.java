package com.majlo.antares.controller;


import com.majlo.antares.dtos.events.EventSeriesDto;
import com.majlo.antares.model.events.Event;
import com.majlo.antares.model.events.EventSeries;
import com.majlo.antares.repository.events.EventSeriesRepository;
import com.majlo.antares.repository.events.EventRepository;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;

@RestController
@RequestMapping("/api/eventseries")
public class EventSeriesController {
    private final EventSeriesRepository eventSeriesRepository;
    private final EventRepository eventRepository;

    public EventSeriesController(EventSeriesRepository eventSeriesRepository, EventRepository eventRepository) {
        this.eventSeriesRepository = eventSeriesRepository;
        this.eventRepository = eventRepository;
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


















    // Test method for creating a sample event series
    @PostMapping("/test_esc")
    public EventSeriesDto createSampleEventSeries() {
        EventSeries eventSeries = EventSeries.builder()
                .name("Sample Event Series")
                .description("This is a sample event series.")
                .organizer("Sample Organizer")
                .build();

        Event event1 = Event.builder()
                .name("Sample Event 1")
                .description("Description for event 1")
                .eventDateStart(LocalDateTime.now().plusDays(1))
                .eventSeries(eventSeries)
                .build();

        Event event2 = Event.builder()
                .name("Sample Event 2")
                .description("Description for event 2")
                .eventDateStart(LocalDateTime.now().plusDays(2))
                .eventSeries(eventSeries)
                .build();

        Event event3 = Event.builder()
                .name("Sample Event 3")
                .description("Description for event 3")
                .eventDateStart(LocalDateTime.now().plusDays(3))
                .eventSeries(eventSeries)
                .build();

        eventSeries.setEvents(new HashSet<>(Arrays.asList(event1, event2, event3)));
        EventSeries savedEventSeries = eventSeriesRepository.save(eventSeries);

        return EventSeriesDto.fromEventSeries(savedEventSeries);
    }

}
