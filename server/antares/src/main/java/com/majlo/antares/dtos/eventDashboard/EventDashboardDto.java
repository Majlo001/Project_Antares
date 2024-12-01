package com.majlo.antares.dtos.eventDashboard;

import com.majlo.antares.model.events.Event;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EventDashboardDto {
    private Long id;
    private String name;
    private String locationName;
    private String locationVariantName;
    private Boolean isPublic;
    private Boolean isSingleEvent;
    private Boolean isEventSeatStatusesCreated;
    private List<SectorForEventDashboardDto> sectors;

    public static EventDashboardDto fromEvent(Event event) {
        return EventDashboardDto.builder()
                .id(event.getId())
                .name(event.getName())
                .locationName(event.getLocationVariant().getLocation().getName())
                .locationVariantName(event.getLocationVariant().getName())
                .isPublic(event.isPublic())
                .isSingleEvent(event.getEventSeries().getIsSingleEvent())
                .isEventSeatStatusesCreated(event.isEventSeatStatusesCreated())
                .sectors(event.getLocationVariant().getSectors().stream().map(sector -> SectorForEventDashboardDto.fromSector(sector, event)).toList())
                .build();
    }

}
