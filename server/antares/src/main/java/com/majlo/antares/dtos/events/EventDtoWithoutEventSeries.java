package com.majlo.antares.dtos.events;

import com.majlo.antares.model.events.Event;
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
public class EventDtoWithoutEventSeries {
    private Long id;
    private String name;
    private String description;
    private EventStatus status;
//    private EventSeries eventSeries;
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
//                .eventSeries(eventSeries)
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

    public static EventDtoWithoutEventSeries fromEvent(Event event) {
        return EventDtoWithoutEventSeries.builder()
                .id(event.getId())
                .name(event.getName())
                .description(event.getDescription())
                .status(event.getStatus())
//                .eventSeries(event.getEventSeries())
                .eventDateStart(event.getEventDateStart())
                .eventDateEnd(event.getEventDateEnd())
                .ticketPurchaseDateEnd(event.getTicketPurchaseDateEnd())
                .ticketPurchaseDateStart(event.getTicketPurchaseDateStart())
                .location(event.getLocation())
                .createdAt(event.getCreatedAt())
                .updatedAt(event.getUpdatedAt())
                .deletedAt(event.getDeletedAt())
                .build();
    }

    public static Set<Event> fromEventDtoSet(Set<EventDtoWithoutEventSeries> eventDtoSet) {
        return eventDtoSet.stream()
                .map(EventDtoWithoutEventSeries::toEvent)
                .collect(Collectors.toSet());
    }
}
