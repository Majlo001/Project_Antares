package com.majlo.antares.dtos.atristDetail;

import com.majlo.antares.dtos.eventsListPreview.EventListPreviewDto;
import com.majlo.antares.model.Artist;
import com.majlo.antares.model.events.Event;
import com.majlo.antares.model.events.EventSeries;
import com.majlo.antares.model.gallery.Gallery;
import com.majlo.antares.repository.events.EventSeriesRepository;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ArtistDto {
    private Long id;
    private String name;
    private String mainImage;
    private String description;
    private Gallery gallery;
    private String websiteUrl;
    private String instagramUrl;
    private String facebookUrl;
    private String spotifyUrl;
    private List<EventListPreviewDto> events;

    public static ArtistDto fromArtist(Artist artist, EventSeriesRepository eventSeriesRepository, Integer maxEvents) {
        List<EventSeries> eventSeriesList = eventSeriesRepository.findAllByArtistsContains(artist);

        List<EventListPreviewDto> eventDtos = eventSeriesList.stream()
                .flatMap(series -> series.getEvents().stream())
//                .filter(Event::isPublic)
                .limit(maxEvents)
                .map(EventListPreviewDto::fromEvent)
                .toList();

        return ArtistDto.builder()
                .id(artist.getId())
                .name(artist.getName())
                .mainImage(artist.getMainImage())
                .description(artist.getDescription())
                .gallery(artist.getGallery())
                .websiteUrl(artist.getWebsiteUrl())
                .instagramUrl(artist.getInstagramUrl())
                .facebookUrl(artist.getFacebookUrl())
                .spotifyUrl(artist.getSpotifyUrl())
                .events(eventDtos)
                .build();
    }

}
