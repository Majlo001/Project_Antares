package com.majlo.antares.dtos.eventStats;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EventStatsDto {
    private Long id;
    private Integer seatsCount;
    private Integer boughtTicketsCount;
    private Double income;
    private List<TicketsPerDayForEventStatsDto> ticketsPerDay;
}
