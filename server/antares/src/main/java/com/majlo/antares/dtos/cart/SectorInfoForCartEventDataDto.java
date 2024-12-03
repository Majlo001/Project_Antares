package com.majlo.antares.dtos.cart;

import com.majlo.antares.dtos.dicts.DictDto;
import com.majlo.antares.model.events.Event;
import com.majlo.antares.model.location.Sector;
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
public class SectorInfoForCartEventDataDto {
    private Long sectorId;
    private String sectorName;
    private List<TicketPriceForCartEventDataDto> tickets;

    public static SectorInfoForCartEventDataDto fromSector(Sector sector, Event event) {
        return SectorInfoForCartEventDataDto.builder()
                .sectorId(sector.getId())
                .sectorName(sector.getName())
                .tickets(sector.getTicketPrices().stream()
                        .filter(ticketPrice -> ticketPrice.getEvent().equals(event))
                        .map(TicketPriceForCartEventDataDto::fromTicketPrice)
                        .toList())
                .build();
    }
}
