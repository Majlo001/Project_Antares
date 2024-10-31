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

    @ManyToOne
    @JoinColumn(name = "row_id")
    private Row row;

//    @OneToMany(mappedBy = "seat")
//    private List<Reservation> reservations;

    private int positionX;
    private int positionY;
    private int positionRotation;
    private boolean seatForDisabled;
}
