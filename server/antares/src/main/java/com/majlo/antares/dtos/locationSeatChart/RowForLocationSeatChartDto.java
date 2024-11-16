package com.majlo.antares.dtos.locationSeatChart;

import com.majlo.antares.model.location.Row;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.stream.Collectors;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RowForLocationSeatChartDto {
    private Long id;
    private int rowNumber;
    private double positionX;
    private double positionY;
    private double positionRotation;
    private List<SeatForLocationSeatChartDto> seats;

    public static RowForLocationSeatChartDto fromRow(Row row) {
        return RowForLocationSeatChartDto.builder()
                .id(row.getId())
                .rowNumber(row.getRowNumber())
                .positionX(row.getPositionX())
                .positionY(row.getPositionY())
                .positionRotation(row.getPositionRotation())
                .seats(row.getSeats().stream()
                        .map(SeatForLocationSeatChartDto::fromSeat)
                        .collect(Collectors.toList()))
                .build();
    }
}
