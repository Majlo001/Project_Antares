package com.majlo.antares.model.events;

import com.majlo.antares.model.location.Location;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

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

    @Lob
    private String description;

    @ManyToOne
    private EventStatus status;

    @ManyToOne
    @JoinColumn(name = "event_series_id")
    private EventSeries eventSeries;

    @ManyToOne
    @JoinColumn(name = "location_id")
    private Location location;


    private LocalDateTime eventDateStart;
    private LocalDateTime eventDateEnd;
    private LocalDateTime ticketPurchaseDateStart;
    private LocalDateTime ticketPurchaseDateEnd;

    private Long maxReservationsPerUser;
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
