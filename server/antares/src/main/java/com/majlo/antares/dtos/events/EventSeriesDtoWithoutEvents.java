package com.majlo.antares.dtos.events;

import com.majlo.antares.model.events.EventCategory;
import com.majlo.antares.model.events.EventSeries;
import com.majlo.antares.model.events.EventTag;
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
public class EventSeriesDtoWithoutEvents {
    private Long id;
    private String name;
    private String description;
    private EventCategory category;
    private String organizer;
    private Set<EventTag> eventTags;

    public EventSeries toEventSeries() {
        EventSeries eventSeries = EventSeries.builder()
                .id(id)
                .name(name)
                .description(description)
                .category(category)
                .eventTags(eventTags)
                .build();
        eventSeries.getEvents().forEach(event -> event.setEventSeries(eventSeries));
        return eventSeries;
    }

    public static EventSeriesDtoWithoutEvents fromEventSeries(EventSeries eventSeries) {
        return EventSeriesDtoWithoutEvents.builder()
                .id(eventSeries.getId())
                .name(eventSeries.getName())
                .description(eventSeries.getDescription())
                .category(eventSeries.getCategory())
                .eventTags(eventSeries.getEventTags())
                .build();
    }
}
