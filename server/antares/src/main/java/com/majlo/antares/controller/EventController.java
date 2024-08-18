package com.majlo.antares.controller;

import com.majlo.antares.dtos.events.EventDto;
import com.majlo.antares.repository.events.EventRepository;
import com.majlo.antares.repository.events.EventSeriesRepository;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/events")
public class EventController {
    private final EventSeriesRepository eventSeriesRepository;
    private final EventRepository eventRepository;

    public EventController(EventSeriesRepository eventSeriesRepository, EventRepository eventRepository) {
        this.eventSeriesRepository = eventSeriesRepository;
        this.eventRepository = eventRepository;
    }

    @GetMapping
    public List<EventDto> getAllEvents() {
        return eventRepository.findAll()
                .stream()
                .map(EventDto::fromEvent)
                .toList();
    }

    @GetMapping("/{id}")
    public EventDto getEvent(@PathVariable Long id) {
        return eventRepository.findById(id)
                .map(EventDto::fromEvent)
                .orElseThrow();
    }
}
