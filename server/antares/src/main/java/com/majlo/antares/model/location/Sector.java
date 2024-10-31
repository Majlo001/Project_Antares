package com.majlo.antares.model.location;

import com.majlo.antares.model.reservation.EventSeatStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Entity
@Data
@Builder
@Table(name = "location_sector")
public class Sector {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;

    /** For standing sectors */
    private boolean isStanding;
    private Integer standingCapacity;

    @ManyToOne
    @JoinColumn(name = "location_variant_id")
    private LocationVariant locationVariant;

    @OneToMany(mappedBy = "sector", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<EventSeatStatus> eventSeatStatuses = new ArrayList<>();

    @OneToMany(mappedBy = "sector", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Row> rows = new ArrayList<>();

    @OneToMany(mappedBy = "sector", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<TicketPrice> ticketPrices = new ArrayList<>();

    public double getTicketPrice(TicketType ticketType) {
        if (ticketPrices.isEmpty()) {
            throw new IllegalArgumentException("No ticket prices found in sector " + name);
        }

        return ticketPrices.stream()
                .filter(ticketPrice -> ticketPrice.getTicketType().equals(ticketType))
                .findFirst()
                .map(TicketPrice::getPrice)
                .orElseThrow(() ->
                        new IllegalArgumentException("Ticket type " + ticketType + " not found in sector " + name)
                );
    }

    public EventSeatStatus findFirstAvailableStandingSeat() {
        return eventSeatStatuses.stream()
                .filter(eventSeatStatus -> eventSeatStatus.getSeat() == null && !eventSeatStatus.isReserved() && !eventSeatStatus.isPaid())
                .findFirst()
                .orElse(null);
    }
}
