package com.majlo.antares.dtos.locationSeatChart;

import com.majlo.antares.dtos.reservation.SeatReservationRequestDto;
import com.majlo.antares.model.location.Seat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SeatForLocationSeatChartDto {
    private Long id;
    private Integer seatNumber;
    private double positionX;
    private double positionY;
    private double positionRotation;
    private boolean seatForDisabled;
    private boolean seatAvailable;

    public static SeatForLocationSeatChartDto fromSeat(Seat seat) {
        return SeatForLocationSeatChartDto.builder()
                .id(seat.getId())
                .seatNumber(seat.getSeatNumber())
                .positionX(seat.getPositionX())
                .positionY(seat.getPositionY())
                .positionRotation(seat.getPositionRotation())
                .seatForDisabled(seat.isSeatForDisabled())
                .seatAvailable(false)
                .build();
    }
}
