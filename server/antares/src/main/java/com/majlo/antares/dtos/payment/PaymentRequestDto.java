package com.majlo.antares.dtos.payment;

import com.majlo.antares.dtos.reservation.SeatReservationRequestDto;
import com.majlo.antares.dtos.reservation.SeatReservationTicketTypeDto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PaymentRequestDto {
    private List<SeatReservationTicketTypeDto> seatReservations;
    private String paymentMethod;
    private String discountCode;
}
