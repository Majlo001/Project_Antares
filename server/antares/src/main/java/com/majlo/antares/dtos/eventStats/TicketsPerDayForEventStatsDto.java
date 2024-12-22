package com.majlo.antares.dtos.eventStats;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TicketsPerDayForEventStatsDto {
    private LocalDate date;
    private Integer amount;
    private Double income;
}
