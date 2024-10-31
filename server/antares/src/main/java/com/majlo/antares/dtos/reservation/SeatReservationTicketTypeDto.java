package com.majlo.antares.dtos.reservation;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SeatReservationTicketTypeDto {
    private Long eventSeatStatusId;
    private Long ticketTypeId;
}
