package com.majlo.antares.dtos.creation;

import com.majlo.antares.model.events.Event;
import com.majlo.antares.model.events.EventSeries;
import com.majlo.antares.model.events.EventStatus;
import com.majlo.antares.model.location.Location;
import com.majlo.antares.model.location.LocationVariant;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EventCreationDto {
    private Long id;
    private String name;
    private boolean isPublic;
    private boolean isSingleEvent;
    private String description;
    private String shortDescription;
    private Long eventStatusId;
    private Long eventSeriesId;
    private Long locationId;
    private Long locationVariantId;
    private LocalDateTime eventDateStart;
    private LocalDateTime eventDateEnd;
    private LocalDateTime ticketPurchaseDateStart;
    private LocalDateTime ticketPurchaseDateEnd;
    private Integer maxReservationsPerUser;
    private Boolean forceChoosingWithoutBreaks;
    private String mainImage;

    public Event toEvent() {
        return Event.builder()
                .id(id)
                .name(name)
                .isPublic(isPublic)
                .description(description)
                .shortDescription(shortDescription)
                .status(EventStatus.builder().id(eventStatusId).build())
                .eventSeries(EventSeries.builder().id(eventSeriesId).build())
                .location(Location.builder().id(locationId).build())
                .locationVariant(LocationVariant.builder().id(locationVariantId).build())
                .eventDateStart(eventDateStart)
                .eventDateEnd(eventDateEnd)
                .ticketPurchaseDateStart(ticketPurchaseDateStart)
                .ticketPurchaseDateEnd(ticketPurchaseDateEnd)
                .maxReservationsPerUser(maxReservationsPerUser)
                .forceChoosingWithoutBreaks(forceChoosingWithoutBreaks)
                .mainImage(mainImage)
                .build();
    }
}
