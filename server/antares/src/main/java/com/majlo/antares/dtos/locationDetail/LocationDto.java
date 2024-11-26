package com.majlo.antares.dtos.locationDetail;

import com.majlo.antares.dtos.eventsListPreview.EventListPreviewDto;
import com.majlo.antares.model.events.Event;
import com.majlo.antares.model.gallery.Gallery;
import com.majlo.antares.model.location.City;
import com.majlo.antares.model.location.Location;
import com.majlo.antares.model.location.LocationType;
import com.majlo.antares.model.location.LocationVariant;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LocationDto {
    private Long id;
    private String mainImage;
    private String name;
    private String cityName;
    private String address;
    private String email;
    private String website;
    private String googleMapsLink;
    private List<EventListPreviewDto> events;
    private String description;


    public static LocationDto fromLocation(Location location, Integer maxEvents) {
        List<Event> events = location.getEvents();
        List<EventListPreviewDto> eventDtos = events.stream()
//                .filter(Event::isPublic)
                .limit(maxEvents)
                .map(EventListPreviewDto::fromEvent)
                .toList();

        return LocationDto.builder()
                .id(location.getId())
                .name(location.getName())
                .description(location.getDescription())
                .address(location.getAddress())
                .cityName(location.getCity().getCityName())
                .email(location.getEmail())
                .website(location.getWebsite())
                .googleMapsLink(location.getGoogleMapsLink())
                .mainImage(location.getMainImage())
                .events(eventDtos)
                .build();
    }
}
