package com.majlo.antares.dtos.dicts;

import com.majlo.antares.model.location.LocationVariant;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LocationVariantDictDto {
    private Long id;
    private String name;

    public static LocationVariantDictDto fromLocationVariant(LocationVariant locationVariant) {
        return LocationVariantDictDto.builder()
                .id(locationVariant.getId())
                .name(locationVariant.getName())
                .build();
    }
}
