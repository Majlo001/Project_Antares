package com.majlo.antares.dtos.cart;

import com.majlo.antares.model.location.TicketPrice;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TicketPriceForCartEventDataDto {
    private Long ticketPriceId;
    private Long ticketTypeId;
    private String ticketTypeName;
    private double price;

    public static TicketPriceForCartEventDataDto fromTicketPrice(TicketPrice ticketPrice) {
        return TicketPriceForCartEventDataDto.builder()
                .ticketPriceId(ticketPrice.getId())
                .ticketTypeId(ticketPrice.getTicketType().getId())
                .ticketTypeName(ticketPrice.getTicketType().getName())
                .price(ticketPrice.getPrice())
                .build();
    }
}
