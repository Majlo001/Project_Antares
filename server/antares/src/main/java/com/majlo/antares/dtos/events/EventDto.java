package com.majlo.antares.dtos.events;

import com.majlo.antares.model.events.Event;
import com.majlo.antares.model.events.EventSeries;
import com.majlo.antares.model.events.EventStatus;
import com.majlo.antares.model.location.Location;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Set;
import java.util.stream.Collectors;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EventDto {
    private Long id;
    private String name;
    private String description;
    private EventStatus status;
    private EventSeriesDtoWithoutEvents eventSeries;
    private LocalDateTime eventDateStart;
    private LocalDateTime eventDateEnd;
    private LocalDateTime ticketPurchaseDateStart;
    private LocalDateTime ticketPurchaseDateEnd;
    private Location location;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private LocalDateTime deletedAt;

    public Event toEvent() {
        return Event.builder()
                .id(id)
                .name(name)
                .description(description)
                .status(status)
                .eventSeries(eventSeries != null ? eventSeries.toEventSeries() : null)
                .eventDateStart(eventDateStart)
                .eventDateEnd(eventDateEnd)
                .ticketPurchaseDateStart(ticketPurchaseDateStart)
                .ticketPurchaseDateEnd(ticketPurchaseDateEnd)
                .location(location)
                .createdAt(createdAt)
                .updatedAt(updatedAt)
                .deletedAt(deletedAt)
                .build();
    }

    public static EventDto fromEvent(Event event) {
        EventSeriesDtoWithoutEvents eventSeriesDtoWithoutEvents = EventSeriesDtoWithoutEvents.fromEventSeries(event.getEventSeries());

        return EventDto.builder()
                .id(event.getId())
                .name(event.getName())
                .description(event.getDescription())
                .status(event.getStatus())
                .eventSeries(eventSeriesDtoWithoutEvents)
                .eventDateStart(event.getEventDateStart())
                .eventDateEnd(event.getEventDateEnd())
                .ticketPurchaseDateStart(event.getTicketPurchaseDateStart())
                .ticketPurchaseDateEnd(event.getTicketPurchaseDateEnd())
                .location(event.getLocation())
                .createdAt(event.getCreatedAt())
                .updatedAt(event.getUpdatedAt())
                .deletedAt(event.getDeletedAt())
                .build();
    }

    public static Set<Event> fromEventDtoSet(Set<EventDto> eventDtoSet) {
        return eventDtoSet.stream()
                .map(EventDto::toEvent)
                .collect(Collectors.toSet());
    }
}
