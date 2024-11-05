package com.majlo.antares.dtos.dicts;

import com.majlo.antares.model.events.EventStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EventStatusDictDto {
    private Long id;
    private String name;

    public static EventStatusDictDto fromEventStatus(EventStatus eventStatus) {
        return EventStatusDictDto.builder()
                .id(eventStatus.getId())
                .name(eventStatus.getEventStatusName())
                .build();
    }
}
