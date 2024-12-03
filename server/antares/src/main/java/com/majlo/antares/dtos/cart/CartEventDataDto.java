package com.majlo.antares.dtos.cart;

import com.majlo.antares.model.events.Event;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.Hibernate;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CartEventDataDto {

    private String eventName;
    private String eventShortDescription;
    private String eventLocation;
    private LocalDateTime eventDateStart;
    private LocalDateTime eventDateEnd;
    private List<SectorInfoForCartEventDataDto> sectorsInfo;


    public static CartEventDataDto fromEvent(Event event, List<Long> selectedSectorsIds) {
        Hibernate.initialize(event);

        return CartEventDataDto.builder()
                .eventName(event.getName())
                .eventShortDescription(event.getShortDescription())
                .eventLocation(event.getLocation().getName())
                .eventDateStart(event.getEventDateStart())
                .eventDateEnd(event.getEventDateEnd())
                .sectorsInfo(event.getLocationVariant().getSectors().stream()
                        .filter(sector -> selectedSectorsIds.contains(sector.getId()))
                        .map(sector -> SectorInfoForCartEventDataDto.fromSector(sector, event))
                        .toList())
                .build();
    }
}
