package com.majlo.antares.dtos.events;

import com.majlo.antares.model.events.Event;
import com.majlo.antares.model.events.EventStatus;
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
    private LocalDateTime dateTime;
    private String location;
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
                .dateTime(dateTime)
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
                .dateTime(event.getDateTime())
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
