package com.majlo.antares.dtos.eventDetail;

import com.majlo.antares.model.events.EventCategory;
import com.majlo.antares.model.events.EventSeries;
import com.majlo.antares.model.events.EventStatus;
import com.majlo.antares.model.events.EventTag;
import com.majlo.antares.model.gallery.Gallery;
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
public class EventSeriesForEventDetailsDto {
    private Long id;
    private String name;
    private Boolean isSingleEvent;
    private String shortDescription;
    private String description;
    private Gallery gallery;
    private EventCategory eventCategory;
    private EventStatus eventSeriesStatus;
    private EventOwnerForEventDetailDto eventOwner;
    private Set<EventTag> eventTags;
    private Set<ArtistForEventDetailDto> artists;

    public static EventSeriesForEventDetailsDto fromEventSeries(EventSeries eventSeries) {
        return EventSeriesForEventDetailsDto.builder()
                .id(eventSeries.getId())
                .name(eventSeries.getName())
                .isSingleEvent(eventSeries.getIsSingleEvent())
                .shortDescription(eventSeries.getShortDescription())
                .description(eventSeries.getDescription())
                .gallery(eventSeries.getGallery())
                .eventCategory(eventSeries.getCategory())
                .eventSeriesStatus(eventSeries.getEventSeriesStatus())
                .eventOwner(EventOwnerForEventDetailDto.fromEventOwner(eventSeries.getEventOwner()))
                .eventTags(eventSeries.getEventTags())
                .artists(eventSeries.getArtists().stream().map(ArtistForEventDetailDto::fromArtist).collect(Collectors.toSet()))
                .build();
    }
}
