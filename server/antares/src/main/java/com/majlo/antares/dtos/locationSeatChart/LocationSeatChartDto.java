package com.majlo.antares.dtos.locationSeatChart;

import com.majlo.antares.model.events.Event;
import com.majlo.antares.model.location.LocationVariant;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

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

    public static LocationSeatChartDto fromLocationVariant(LocationVariant locationVariant, Long eventId) {
        Event event = locationVariant.getLocation().getEvents().stream()
                .filter(e -> e.getId().equals(eventId))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Event not found"));

        return LocationSeatChartDto.builder()
                .locationVariantId(locationVariant.getId())
                .locationName(locationVariant.getLocation().getName())
                .locationVariantName(locationVariant.getName())
                .sectors(locationVariant.getSectors().stream()
                        .map(SectorForLocationSeatChartDto::fromSector)
                        .toList())
                .maxReservationsPerUser(event.getMaxReservationsPerUser())
                .forceChoosingWithoutBreaks(event.getForceChoosingWithoutBreaks())
                .build();
    }
}
