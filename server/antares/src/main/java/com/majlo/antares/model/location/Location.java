package com.majlo.antares.model.location;

import com.majlo.antares.model.events.Event;
import com.majlo.antares.model.gallery.Gallery;
import com.majlo.antares.model.gallery.GalleryItem;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Set;

@AllArgsConstructor
@NoArgsConstructor
@Entity
@Data
@Builder
@Table(name = "location")
public class Location {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String mainImage;
    private String name;

    @ManyToOne
    @JoinColumn(name = "city_id")
    private City city;
    private String address;
    private String postalCode;
    private String phoneNumber;
    private String email;
    private String website;
    private String googleMapsLink;

    @OneToMany(mappedBy = "location")
    private List<Event> events;

    @Lob
    private String description;

    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Gallery gallery;

    @ManyToOne
    @JoinColumn(name = "location_type_id")
    private LocationType locationType;

    @OneToMany(mappedBy = "location")
    private List<LocationVariant> locationVariantList;

    @OneToMany(mappedBy = "location", cascade = CascadeType.ALL)
    private List<LocationContactPerson> contactPersons;
}
