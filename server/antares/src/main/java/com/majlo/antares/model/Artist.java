package com.majlo.antares.model;

import com.majlo.antares.model.gallery.Gallery;
import com.majlo.antares.model.gallery.GalleryItem;
import jakarta.persistence.*;
import lombok.*;

import java.util.Set;

@AllArgsConstructor
@NoArgsConstructor
@Entity
@Getter
@Setter
@Builder
@Table(name = "event_artist")
public class Artist {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String mainImage;

    @Lob
    private String description;

    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Gallery gallery;

    private String websiteUrl;
}
