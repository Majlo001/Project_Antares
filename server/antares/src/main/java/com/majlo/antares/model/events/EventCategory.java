package com.majlo.antares.model.events;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Entity
@Data
@Builder
@Table(name = "event_category")
public class EventCategory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long eventCategoryId;

    @NotEmpty(message = "Event category cannot be empty")
    @NotNull
    private String eventCategoryName;
}
