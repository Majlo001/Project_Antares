package com.majlo.antares.dtos.eventDashboard;

import com.majlo.antares.model.events.Event;
import com.majlo.antares.model.location.Sector;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SectorForEventDashboardDto {
    private Long id;
    private String name;
    private Boolean isStanding;
    private List<TicketPriceForEventDashboardDto> ticketPrices;

    public static SectorForEventDashboardDto fromSector(Sector sector, Event event) {
        return SectorForEventDashboardDto.builder()
                .id(sector.getId())
                .name(sector.getName())
                .isStanding(sector.isStanding())
                .ticketPrices(sector.getTicketPrices().stream().filter(ticketPrice -> ticketPrice.getEvent().equals(event)).map(TicketPriceForEventDashboardDto::fromTicketPrice).toList())
                .build();
    }
}
