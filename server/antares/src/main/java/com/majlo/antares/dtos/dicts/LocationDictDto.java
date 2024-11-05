package com.majlo.antares.dtos.dicts;

import com.majlo.antares.model.location.Location;
import com.majlo.antares.model.location.LocationVariant;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LocationDictDto {
    private Long id;
    private String name;

    public static LocationDictDto fromLocation(Location location) {
        return LocationDictDto.builder()
                .id(location.getId())
                .name(location.getName())
                .build();
    }
}
