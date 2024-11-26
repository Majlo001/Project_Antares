package com.majlo.antares.dtos.eventDetail;

import com.majlo.antares.dtos.eventDetail.EventSeriesForEventDetailsDto;
import com.majlo.antares.dtos.events.LocationForEventPreviewDto;
import com.majlo.antares.model.events.Event;
import com.majlo.antares.model.events.EventStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EventDetailDto {
    private Long id;
    private String name;
    private String mainImage;
    private String description;
    private String shortDescription;
    private EventStatus status;
    private EventSeriesForEventDetailsDto eventSeries;
    private LocalDateTime eventDateStart;
    private LocalDateTime eventDateEnd;
    private LocalDateTime ticketPurchaseDateStart;
    private LocalDateTime ticketPurchaseDateEnd;
    private LocationForEventPreviewDto location;

    public static EventDetailDto fromEvent(Event event) {
        return EventDetailDto.builder()
                .id(event.getId())
                .name(event.getName())
                .mainImage(event.getMainImage())
                .description(event.getDescription())
                .shortDescription(event.getShortDescription())
                .status(event.getStatus())
                .eventSeries(EventSeriesForEventDetailsDto.fromEventSeries(event.getEventSeries()))
                .eventDateStart(event.getEventDateStart())
                .eventDateEnd(event.getEventDateEnd())
                .ticketPurchaseDateStart(event.getTicketPurchaseDateStart())
                .ticketPurchaseDateEnd(event.getTicketPurchaseDateEnd())
                .location(LocationForEventPreviewDto.fromLocation(event.getLocation()))
                .build();
    }
}
