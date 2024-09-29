package com.majlo.antares.dtos.reservation;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SeatReservationRequestDto {
    private Long seatId;
    private Long eventId;
}
