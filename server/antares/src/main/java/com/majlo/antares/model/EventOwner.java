package com.majlo.antares.model;

import com.majlo.antares.model.events.Event;
import com.majlo.antares.model.events.EventSeries;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Entity
@Getter
@Setter
@Builder
@Table(name = "antares_event_owner")
public class EventOwner {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String image;

    @NotNull
    @OneToOne
    @JoinColumn(name = "user_id")
    private User eventOwner;

    @OneToMany(mappedBy = "eventOwner")
    private List<Event> events;

    @OneToMany(mappedBy = "eventOwner")
    private List<EventSeries> eventSeries;
}
