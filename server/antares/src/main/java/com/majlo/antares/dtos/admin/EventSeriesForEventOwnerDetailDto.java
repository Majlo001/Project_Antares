package com.majlo.antares.dtos.admin;

import com.majlo.antares.model.Artist;
import com.majlo.antares.model.events.Event;
import com.majlo.antares.model.events.EventCategory;
import com.majlo.antares.model.events.EventSeries;
import com.majlo.antares.model.events.EventTag;
import com.majlo.antares.repository.events.ArtistRepostiory;
import com.majlo.antares.repository.events.EventTagRepository;
import jakarta.persistence.EntityManager;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EventSeriesForEventOwnerDetailDto {
    private Long id;
    private String name;
    private String description;
    private String shortDescription;
    private Long eventCategoryId;
    private String youtubePreviewUrl;
    private Set<String> eventTags;
    private Set<Long> artistsIds;

    public static EventSeriesForEventOwnerDetailDto fromEventSeries(EventSeries eventSeries) {
        if (eventSeries.getIsSingleEvent())
            return EventSeriesForEventOwnerDetailDto.builder()
                    .id(eventSeries.getId())
                    .name(eventSeries.getName())
                    .description(eventSeries.getDescription())
                    .shortDescription(eventSeries.getShortDescription())
                    .eventCategoryId(eventSeries.getCategory().getEventCategoryId())
                    .youtubePreviewUrl(eventSeries.getYoutubePreviewUrl())
                    .eventTags(eventSeries.getEventSeriesTagStrings())
                    .artistsIds(eventSeries.getArtists().stream().map(Artist::getId).collect(java.util.stream.Collectors.toSet()))
                    .build();
        else
            return null;
    }

    public EventSeries toEventSeries(EventTagRepository eventTagRepository, ArtistRepostiory artistRepostiory) {
        EventSeries eventSeries = EventSeries.builder()
                .id(id)
                .name(name)
                .description(description)
                .shortDescription(shortDescription)
                .category(EventCategory.builder().eventCategoryId(eventCategoryId).build())
                .youtubePreviewUrl(youtubePreviewUrl)
                .build();

        eventSeries.setEventTagsFromStrings(eventTags, eventTagRepository);
        eventSeries.setArtistsFromIds(artistsIds, artistRepostiory);

        return eventSeries;
    }
}
