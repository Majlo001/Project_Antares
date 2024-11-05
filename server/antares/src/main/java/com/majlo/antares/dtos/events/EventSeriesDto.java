package com.majlo.antares.dtos.events;

import com.majlo.antares.model.events.EventCategory;
import com.majlo.antares.model.events.EventSeries;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;
import java.util.stream.Collectors;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EventSeriesDto {
    private Long id;
    private String name;
    private String description;
    private EventCategory category;
    private Set<EventDtoWithoutEventSeries> events;
//    private Set<EventTag> eventTags;

    public EventSeries toEventSeries() {
        EventSeries eventSeries = EventSeries.builder()
                .id(id)
                .name(name)
                .description(description)
                .category(category)
                .events(EventDtoWithoutEventSeries.fromEventDtoSet(events))
                .build();
        eventSeries.getEvents().forEach(event -> event.setEventSeries(eventSeries));
        return eventSeries;
    }

    public static EventSeriesDto fromEventSeries(EventSeries eventSeries) {
        return EventSeriesDto.builder()
                .id(eventSeries.getId())
                .name(eventSeries.getName())
                .description(eventSeries.getDescription())
                .category(eventSeries.getCategory())
                .events(eventSeries.getEvents().stream()
                        .map(EventDtoWithoutEventSeries::fromEvent)
                        .collect(Collectors.toSet()))
                .build();
    }
}
