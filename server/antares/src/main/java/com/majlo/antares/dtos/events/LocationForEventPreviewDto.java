package com.majlo.antares.dtos.events;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LocationForEventPreviewDto {
    private Long id;
    private String mainImage;
    private String name;
    private String address;
    private String city;
    private String country;
    private String website;
    private String googleMapsLink;
    private String description;

    public static LocationForEventPreviewDto fromLocation(com.majlo.antares.model.location.Location location) {
        return LocationForEventPreviewDto.builder()
                .id(location.getId())
                .mainImage(location.getMainImage())
                .name(location.getName())
                .address(location.getAddress())
                .city(location.getCity())
                .country(location.getCountry())
                .website(location.getWebsite())
                .googleMapsLink(location.getGoogleMapsLink())
                .description(location.getDescription())
                .build();
    }
}
