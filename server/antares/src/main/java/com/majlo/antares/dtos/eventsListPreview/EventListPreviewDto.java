package com.majlo.antares.dtos.eventsListPreview;

import com.majlo.antares.dtos.events.EventSeriesDtoWithoutEvents;
import com.majlo.antares.model.events.Event;
import com.majlo.antares.model.events.EventStatus;
import com.majlo.antares.model.location.Location;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EventListPreviewDto {
    private Long id;
    private String mainImage;
    private String name;
    private String shortDescription;
    private EventStatus status;
    private EventSeriesDtoWithoutEvents eventSeries;
    private LocalDateTime eventDateStart;
    private LocalDateTime eventDateEnd;
    private LocalDateTime ticketPurchaseDateStart;
    private LocalDateTime ticketPurchaseDateEnd;
    private LocationForEventListPreviewDto location;

    public static EventListPreviewDto fromEvent(Event event) {
        return EventListPreviewDto.builder()
                .id(event.getId())
                .mainImage(event.getMainImage())
                .name(event.getName())
                .shortDescription(event.getShortDescription())
                .status(event.getStatus())
                .eventSeries(EventSeriesDtoWithoutEvents.fromEventSeries(event.getEventSeries()))
                .eventDateStart(event.getEventDateStart())
                .eventDateEnd(event.getEventDateEnd())
                .ticketPurchaseDateStart(event.getTicketPurchaseDateStart())
                .ticketPurchaseDateEnd(event.getTicketPurchaseDateEnd())
                .location(LocationForEventListPreviewDto.fromLocation(event.getLocation()))
                .build();
    }
}
