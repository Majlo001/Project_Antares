package com.majlo.antares.model.responses;

import com.majlo.antares.dtos.events.EventDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EventResponseDto {
    private List<EventDto> events;
    private int pageNo;
    private int pageSize;
    private long totalElements;
    private int totalPages;
}
