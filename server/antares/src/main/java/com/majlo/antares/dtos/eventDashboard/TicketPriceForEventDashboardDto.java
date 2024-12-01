package com.majlo.antares.dtos.eventDashboard;

import com.majlo.antares.model.events.Event;
import com.majlo.antares.model.location.TicketPrice;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TicketPriceForEventDashboardDto {
    private Long id;
    private double price;
    private String ticketTypeName;
    private Long ticketTypeId;

    public static TicketPriceForEventDashboardDto fromTicketPrice(TicketPrice ticketPrice) {
        return TicketPriceForEventDashboardDto.builder()
                .id(ticketPrice.getId())
                .price(ticketPrice.getPrice())
                .ticketTypeName(ticketPrice.getTicketType().getName())
                .ticketTypeId(ticketPrice.getTicketType().getId())
                .build();
    }
}
