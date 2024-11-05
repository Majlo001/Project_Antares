package com.majlo.antares.dtos.dicts;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EventSeriesDictDto {
    private Long id;
    private String name;

    public static EventSeriesDictDto fromEventSeries(com.majlo.antares.model.events.EventSeries eventSeries) {
        return EventSeriesDictDto.builder()
                .id(eventSeries.getId())
                .name(eventSeries.getName())
                .build();
    }
}
