package com.majlo.antares.model.events;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@AllArgsConstructor
@NoArgsConstructor
@Entity
@Data
@Builder
@Table(name = "event_tag")
public class EventTag {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long eventTagId;

    @NotEmpty(message = "Tag name cannot be empty")
    @NotNull
    private String tagName;

    @ManyToMany(mappedBy = "eventTags")
    private Set<EventSeries> events;
}