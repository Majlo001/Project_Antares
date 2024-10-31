package com.majlo.antares.model.events;

import com.majlo.antares.model.location.Location;
import com.majlo.antares.model.location.LocationVariant;
import com.majlo.antares.model.reservation.EventSeatStatus;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Entity
@Data
@Builder
@Table(name = "antares_event")
public class Event {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private boolean isPublic;

    @Lob
    private String description;

    @ManyToOne
    private EventStatus status;

    @OneToMany(mappedBy = "event",
            cascade = CascadeType.ALL,
            orphanRemoval = true)
    private List<EventSeatStatus> eventSeatStatuses;

    @ManyToOne
    @JoinColumn(name = "event_series_id")
    private EventSeries eventSeries;

    @ManyToOne
    @JoinColumn(name = "location_id")
    private Location location;

    @ManyToOne
    @JoinColumn(name = "location_variant_id")
    private LocationVariant locationVariant;


    private LocalDateTime eventDateStart;
    private LocalDateTime eventDateEnd;
    private LocalDateTime ticketPurchaseDateStart;
    private LocalDateTime ticketPurchaseDateEnd;

    private Integer maxReservationsPerUser;
    private Boolean forceChoosingWithoutBreaks;

//    private String image;
//    private String link;
//    private String price;
//    private String capacity;
//    private String registered;


    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private LocalDateTime archivedAt;
    private LocalDateTime deletedAt;    // TODO: Implement soft delete
}
