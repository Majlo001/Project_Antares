package com.majlo.antares.model.location;

import com.majlo.antares.model.events.Event;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

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
    private String address;
    private String city;
    private String country;
    private String postalCode;
    private String phoneNumber;
    private String email;
    private String website;
    private String googleMapsLink;

    @OneToMany(mappedBy = "location")
    private List<Event> events;

    @Lob
    private String description;

    @ElementCollection
    private List<String> gallery;

    @ManyToOne
    @JoinColumn(name = "location_type_id")
    private LocationType locationType;

    @OneToMany(mappedBy = "location")
    private List<LocationVariant> locationVariantList;

    @OneToMany(mappedBy = "location", cascade = CascadeType.ALL)
    private List<LocationContactPerson> contactPersons;
}
