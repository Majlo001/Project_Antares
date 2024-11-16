package com.majlo.antares.dtos.eventDetail;

import com.majlo.antares.model.EventOwner;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EventOwnerForEventDetailDto {
    private Long id;
    private String name;
    private String image;

    public static EventOwnerForEventDetailDto fromEventOwner(EventOwner eventOwner) {
        return EventOwnerForEventDetailDto.builder()
                .id(eventOwner.getId())
                .name(eventOwner.getName())
                .image(eventOwner.getImage())
                .build();
    }
}
