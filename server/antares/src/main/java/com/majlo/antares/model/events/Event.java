package com.majlo.antares.model.events;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Entity
@Data
@Builder
@Table(name = "antares_event")
@EqualsAndHashCode(exclude = "eventSeries")
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
//    @JsonBackReference
    @JoinColumn(name = "event_series_id")
    private EventSeries eventSeries;

    private LocalDateTime dateTime;
    private String location;            //  TODO: Change to Location object

//    private String image;
//    private String link;
//    private String price;
//    private String capacity;
//    private String registered;


    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private LocalDateTime deletedAt;
}
