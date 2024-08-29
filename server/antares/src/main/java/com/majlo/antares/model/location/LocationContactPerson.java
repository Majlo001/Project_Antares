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
    @Table(name = "location_contact_person")
    public class LocationContactPerson {
        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private Long id;
        private String name;
        private String surname;
        private String position;
        private String department;
        private String phoneNumber1;
        private String phoneNumber2;
        private String email;

        @ManyToOne
        @JoinColumn(name = "location_id")
        private Location location;
    }
