package com.majlo.antares.dtos.locationSeatChart;

import com.majlo.antares.model.events.Event;
import com.majlo.antares.model.location.LocationVariant;
import com.majlo.antares.repository.events.EventRepository;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.jpa.repository.support.SimpleJpaRepository;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LocationSeatChartDto {

    private Long locationVariantId;
    private String locationName;
    private String locationVariantName;
    private List<SectorForLocationSeatChartDto> sectors;
    private Integer maxReservationsPerUser;
    private Boolean forceChoosingWithoutBreaks;

    public static LocationSeatChartDto fromLocationVariant(LocationVariant locationVariant, Event event) {
        return LocationSeatChartDto.builder()
                .locationVariantId(locationVariant.getId())
                .locationName(locationVariant.getLocation().getName())
                .locationVariantName(locationVariant.getName())
                .sectors(locationVariant.getSectors().stream()
                        .map(sector -> SectorForLocationSeatChartDto.fromSector(sector, event))
                        .toList())
                .maxReservationsPerUser(event.getMaxReservationsPerUser())
                .forceChoosingWithoutBreaks(event.getForceChoosingWithoutBreaks())
                .build();
    }
}
