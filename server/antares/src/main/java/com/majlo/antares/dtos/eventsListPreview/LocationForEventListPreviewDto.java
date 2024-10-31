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
    private String mainImage;
    private LocationType locationType;

    public static LocationForEventListPreviewDto fromLocation(Location location) {
        return LocationForEventListPreviewDto.builder()
                .id(location.getId())
                .mainImage(location.getMainImage())
                .locationType(location.getLocationType())
                .build();
    }
}
