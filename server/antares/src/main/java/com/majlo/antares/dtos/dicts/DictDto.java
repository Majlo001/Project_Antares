package com.majlo.antares.dtos.dicts;

import com.majlo.antares.model.events.EventCategory;
import com.majlo.antares.model.events.EventSeries;
import com.majlo.antares.model.events.EventStatus;
import com.majlo.antares.model.events.EventTag;
import com.majlo.antares.model.location.City;
import com.majlo.antares.model.location.Location;
import com.majlo.antares.model.location.LocationVariant;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DictDto {
    private Long id;
    private String name;



    public static DictDto fromCity(City city) {
        return DictDto.builder()
                .id(city.getId())
                .name(city.getCityName())
                .build();
    }

    public static DictDto fromEventCategory(EventCategory eventCategory) {
        return DictDto.builder()
                .id(eventCategory.getEventCategoryId())
                .name(eventCategory.getEventCategoryName())
                .build();
    }

    public static DictDto fromEventStatus(EventStatus eventStatus) {
        return DictDto.builder()
                .id(eventStatus.getId())
                .name(eventStatus.getEventStatusName())
                .build();
    }

    public static DictDto fromEventSeries(EventSeries eventSeries) {
        return DictDto.builder()
                .id(eventSeries.getId())
                .name(eventSeries.getName())
                .build();
    }

    public static DictDto fromEventTag(EventTag eventTag) {
        return DictDto.builder()
                .id(eventTag.getEventTagId())
                .name(eventTag.getTagName())
                .build();
    }

    public static DictDto fromLocation(Location location) {
        return DictDto.builder()
                .id(location.getId())
                .name(location.getName())
                .build();
    }

    public static DictDto fromLocationVariant(LocationVariant locationVariant) {
        return DictDto.builder()
                .id(locationVariant.getId())
                .name(locationVariant.getName())
                .build();
    }
}
