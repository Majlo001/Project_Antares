package com.majlo.antares.model.location;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Entity
@Data
@Builder
@Table(name = "location_seat")
public class Seat {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Integer seatNumber;

    /** For Positions */
    private double positionX;
    private double positionY;
    private double positionRotation;


    @ManyToOne
    @JoinColumn(name = "row_id")
    private Row row;

//    @OneToMany(mappedBy = "seat")
//    private List<Reservation> reservations;

    private boolean seatForDisabled;
}
