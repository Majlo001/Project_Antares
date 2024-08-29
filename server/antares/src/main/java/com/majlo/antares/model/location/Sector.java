package com.majlo.antares.model.location;

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
@Table(name = "location_sector")
public class Sector {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String type; // TODO: np. siedzące/stojące

    @ManyToOne
    @JoinColumn(name = "location_variant_id")
    private LocationVariant locationVariant;

    @OneToMany(mappedBy = "sector")
    private List<Row> rows;
}
