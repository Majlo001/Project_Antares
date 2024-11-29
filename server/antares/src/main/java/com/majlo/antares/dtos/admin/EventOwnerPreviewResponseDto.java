package com.majlo.antares.dtos.admin;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EventOwnerPreviewResponseDto {
    private List<EventOwnerPreviewDto> events;
    private int pageNo;
    private int pageSize;
    private long totalElements;
    private int totalPages;
}