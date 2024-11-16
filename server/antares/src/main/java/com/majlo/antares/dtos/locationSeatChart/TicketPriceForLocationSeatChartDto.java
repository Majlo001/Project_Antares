package com.majlo.antares.dtos.locationSeatChart;

import com.majlo.antares.model.location.TicketPrice;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TicketPriceForLocationSeatChartDto {
    private Long id;
    private double price;
    private String ticketType;
    private String ticketTypeDescription;

    public static TicketPriceForLocationSeatChartDto fromTicketPrice(TicketPrice ticketPrice) {
        return TicketPriceForLocationSeatChartDto.builder()
                .id(ticketPrice.getId())
                .price(ticketPrice.getPrice())
                .ticketType(ticketPrice.getTicketType().getName())
                .ticketTypeDescription(ticketPrice.getTicketType().getDescription())
                .build();
    }
}
