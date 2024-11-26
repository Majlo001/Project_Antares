package com.majlo.antares.dtos.eventsListPreview;

import com.majlo.antares.model.location.Location;
import com.majlo.antares.model.location.LocationType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LocationForEventListPreviewDto {
    private Long id;
    private String name;
    private String city;
    private LocationType locationType;

    public static LocationForEventListPreviewDto fromLocation(Location location) {
        return LocationForEventListPreviewDto.builder()
                .id(location.getId())
                .name(location.getName())
                .city(location.getCity().getCityName())
                .locationType(location.getLocationType())
                .build();
    }
}
