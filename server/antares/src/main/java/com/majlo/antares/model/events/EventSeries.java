package com.majlo.antares.model.events;

import com.majlo.antares.model.Artist;
import com.majlo.antares.model.EventOwner;
import com.majlo.antares.model.gallery.Gallery;
import com.majlo.antares.model.gallery.GalleryItem;
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
    private Boolean isSingleEvent;
    private String shortDescription;

    @Lob
    private String description;

    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Gallery gallery;

    @ManyToOne
    private EventCategory category;

    @ManyToOne
    private EventStatus eventSeriesStatus;

    @ManyToOne
    private EventOwner eventOwner;

    @OneToMany(
            mappedBy = "eventSeries",
            cascade = CascadeType.ALL,
            orphanRemoval = true,
            fetch = FetchType.EAGER)
    private Set<Event> events;


    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "event_series_tags",
            joinColumns = @JoinColumn(name = "event_series_id"),
            inverseJoinColumns = @JoinColumn(name = "tag_id"))
    private Set<EventTag> eventTags;


    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "event_series_artists",
            joinColumns = @JoinColumn(name = "event_series_id"),
            inverseJoinColumns = @JoinColumn(name = "artist_id"))
    private Set<Artist> artists;
}
