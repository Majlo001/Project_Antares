package com.majlo.antares.model.events;

import com.majlo.antares.model.Artist;
import com.majlo.antares.model.EventOwner;
import com.majlo.antares.model.gallery.Gallery;
import com.majlo.antares.model.gallery.GalleryItem;
import com.majlo.antares.repository.events.ArtistRepostiory;
import com.majlo.antares.repository.events.EventTagRepository;
import jakarta.persistence.*;
import jakarta.transaction.Transactional;
import lombok.*;

import java.util.HashSet;
import java.util.List;
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
    private String youtubePreviewUrl;

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


    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "event_series_tags",
            joinColumns = @JoinColumn(name = "event_series_id"),
            inverseJoinColumns = @JoinColumn(name = "tag_id"))
    private Set<EventTag> eventTags = new HashSet<>();


    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "event_series_artists",
            joinColumns = @JoinColumn(name = "event_series_id"),
            inverseJoinColumns = @JoinColumn(name = "artist_id"))
    private Set<Artist> artists = new HashSet<>();



    @Transactional
    public void setEventTagsFromStrings(Set<String> tags, EventTagRepository eventTagRepository) {
        if (this.eventTags == null) {
            this.eventTags = new HashSet<>();
        }
        else {
            this.eventTags.clear();
        }


        for (String tagName : tags) {
            EventTag existingTag = eventTagRepository.findByTagName(tagName);

            /** Create new tag if it doesn't exist */
            if (existingTag == null) {
                existingTag = new EventTag();
                existingTag.setTagName(tagName);
                eventTagRepository.save(existingTag);
            }

            this.eventTags.add(existingTag);
        }
    }

    @Transactional
    public void setArtistsFromIds(Set<Long> artistsIds, ArtistRepostiory artistRepostiory) {
        if (this.artists == null) {
            this.artists = new HashSet<>();
        }
        else {
            this.artists.clear();
        }

        for (Long artistId : artistsIds) {
            Artist artist = artistRepostiory.findById(artistId).orElse(null);
            if (artist != null) {
                this.artists.add(artist);
            }
        }
    }

    @Transactional
    public Set<String> getEventSeriesTagStrings() {
        Set<String> stringTags = new HashSet<>();
        if (this.eventTags == null) {
            return stringTags;
        }

        for (EventTag tag : this.eventTags) {
            stringTags.add(tag.getTagName());
        }

        return stringTags;
    }
}
