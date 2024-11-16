package com.majlo.antares.dtos.locationSeatChart;

import com.majlo.antares.model.location.Row;
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
public class SectorForLocationSeatChartDto {
    private Long id;
    private String name;
    private List<Sector.CHPoints> convexHullPoints;
    private double positionX;
    private double positionY;
    private boolean isStanding;
    private Integer standingCapacity;
    private List<RowForLocationSeatChartDto> rows;
    private List<TicketPriceForLocationSeatChartDto> ticketPrices;

    public static SectorForLocationSeatChartDto fromSector(Sector sector) {
        return SectorForLocationSeatChartDto.builder()
                .id(sector.getId())
                .name(sector.getName())
                .convexHullPoints(sector.getConvexHullPoints())
                .positionX(sector.getPositionX())
                .positionY(sector.getPositionY())
                .isStanding(sector.isStanding())
                .standingCapacity(sector.getStandingCapacity())
                .rows(sector.getRows().stream()
                        .map(RowForLocationSeatChartDto::fromRow)
                        .toList())
                .ticketPrices(sector.getTicketPrices().stream()
                        .map(TicketPriceForLocationSeatChartDto::fromTicketPrice)
                        .toList())
                .build();
    }
}
