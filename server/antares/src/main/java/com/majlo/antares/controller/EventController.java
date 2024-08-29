package com.majlo.antares.controller;

import com.majlo.antares.dtos.events.EventDto;
import com.majlo.antares.model.events.Event;
import com.majlo.antares.model.events.EventSeries;
import com.majlo.antares.model.location.Location;
import com.majlo.antares.model.responses.EventResponseDto;
import com.majlo.antares.repository.events.EventRepository;
import com.majlo.antares.repository.events.EventSeriesRepository;
import com.majlo.antares.specifications.EventResponseSpecification;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
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
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "30") int size
    ) {
        Pageable pageable = PageRequest.of(page, size);

        Specification<Event> specification = EventResponseSpecification.filterByCriteria(location, dateStart, dateEnd, tag);
        Page<Event> eventPage = eventRepository.findAll(specification, pageable);

        List<EventDto> eventDtos = eventPage.stream().map(EventDto::fromEvent).toList();

        return EventResponseDto.builder()
                .events(eventDtos)
                .pageNo(eventPage.getNumber() + 1)
                .pageSize(eventPage.getSize())
                .totalElements(eventPage.getTotalElements())
                .totalPages(eventPage.getTotalPages())
                .build();
    }

    @GetMapping("/{id}")
    public EventDto getEvent(@PathVariable Long id) {
        return eventRepository.findById(id)
                .map(EventDto::fromEvent)
                .orElseThrow();
    }
}
