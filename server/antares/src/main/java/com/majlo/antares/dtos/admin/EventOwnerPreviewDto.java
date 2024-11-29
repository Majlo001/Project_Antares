package com.majlo.antares.dtos.admin;

import com.majlo.antares.model.events.Event;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EventOwnerPreviewDto {
    private Long eventId;
    private String eventName;
    private LocalDateTime eventDateStart;
    private String city;
    private String category;
    private String status;
    private Boolean isPublic;

    public static EventOwnerPreviewDto fromEvent(Event event) {
        return EventOwnerPreviewDto.builder()
                .eventId(event.getId())
                .eventName(event.getName())
                .eventDateStart(event.getEventDateStart())
                .city(event.getLocation().getCity().getCityName())
                .category(event.getEventSeries().getCategory().getEventCategoryName())
                .status(event.getStatus().getEventStatusName())
                .isPublic(event.isPublic())
                .build();
    }
}
