package com.majlo.antares.dtos.admin;

import com.majlo.antares.dtos.creation.EventCreationDto;
import com.majlo.antares.model.events.Event;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EventOwnerDetailDto {
    private Long id;
    private String name;
    private boolean isPublic;
    private boolean isSingleEvent;
    private String description;
    private String shortDescription;
    private Long eventStatusId;
    private String eventStatusName;
    private Long eventSeriesId;
    private String eventSeriesName;
    private Long locationId;
    private String locationName;
    private Long locationVariantId;
    private String locationVariantName;
    private LocalDateTime eventDateStart;
    private LocalDateTime eventDateEnd;
    private LocalDateTime ticketPurchaseDateStart;
    private LocalDateTime ticketPurchaseDateEnd;
    private Integer maxReservationsPerUser;
    private Boolean forceChoosingWithoutBreaks;
    private String mainImage;

    public static EventOwnerDetailDto fromEvent(Event event) {
        return EventOwnerDetailDto.builder()
                .id(event.getId())
                .name(event.getName())
                .isPublic(event.isPublic())
                .isSingleEvent(event.getEventSeries().getIsSingleEvent())
                .description(event.getDescription())
                .shortDescription(event.getShortDescription())
                .eventStatusId(event.getStatus().getId())
                .eventStatusName(event.getStatus().getEventStatusName())
                .eventSeriesId(event.getEventSeries().getId())
                .eventSeriesName(event.getEventSeries().getName())
                .locationId(event.getLocation().getId())
                .locationName(event.getLocation().getName())
                .locationVariantId(event.getLocationVariant().getId())
                .locationVariantName(event.getLocationVariant().getName())
                .eventDateStart(event.getEventDateStart())
                .eventDateEnd(event.getEventDateEnd())
                .ticketPurchaseDateStart(event.getTicketPurchaseDateStart())
                .ticketPurchaseDateEnd(event.getTicketPurchaseDateEnd())
                .maxReservationsPerUser(event.getMaxReservationsPerUser())
                .forceChoosingWithoutBreaks(event.getForceChoosingWithoutBreaks())
                .mainImage(event.getMainImage())
                .build();
    }
}
