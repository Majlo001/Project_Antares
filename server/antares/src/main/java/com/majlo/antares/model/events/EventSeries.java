package com.majlo.antares.model.events;

import jakarta.persistence.*;
import lombok.*;

import java.util.Set;

@AllArgsConstructor
@NoArgsConstructor
@Entity
@Data
@Builder
@Table(name = "antares_event_series")
@EqualsAndHashCode(exclude = "events")
public class EventSeries {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;

    @Lob
    private String description;

    @ManyToOne
    private EventCategory category;

    private String organizer;           // TODO: Change to User / EventOrganiser object

    @OneToMany(
            mappedBy = "eventSeries",
            cascade = CascadeType.ALL,
            orphanRemoval = true,
            fetch = FetchType.EAGER)
//    @JsonManagedReference
    private Set<Event> events;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "event_series_tags",
            joinColumns = @JoinColumn(name = "event_series_id"),
            inverseJoinColumns = @JoinColumn(name = "tag_id"))
    private Set<EventTag> eventTags;
}
