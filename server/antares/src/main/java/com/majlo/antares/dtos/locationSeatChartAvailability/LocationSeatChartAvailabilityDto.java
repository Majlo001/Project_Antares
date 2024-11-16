package com.majlo.antares.dtos.locationSeatChartAvailability;

import com.majlo.antares.model.reservation.EventSeatStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LocationSeatChartAvailabilityDto {
    private Long seatId;
    private Long sectorId;
    private Long rowId;
    private Long id;
    private boolean isAvailable;

    public static LocationSeatChartAvailabilityDto fromEventSeatStatus(EventSeatStatus eventSeatStatus) {
        return LocationSeatChartAvailabilityDto.builder()
                .seatId(eventSeatStatus.getSeat().getId())
                .sectorId(eventSeatStatus.getSeat().getRow().getSector().getId())
                .rowId(eventSeatStatus.getSeat().getRow().getId())
                .id(eventSeatStatus.getId())
                .isAvailable(!(eventSeatStatus.isReserved() || eventSeatStatus.isPaid()))
                .build();
    }
}
