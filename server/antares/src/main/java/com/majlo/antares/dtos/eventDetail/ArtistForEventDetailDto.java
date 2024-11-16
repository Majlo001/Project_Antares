package com.majlo.antares.dtos.eventDetail;

import com.majlo.antares.model.Artist;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ArtistForEventDetailDto {
    private Long id;
    private String name;
    private String mainImage;

    public static ArtistForEventDetailDto fromArtist(Artist artist) {
        return ArtistForEventDetailDto.builder()
                .id(artist.getId())
                .name(artist.getName())
                .mainImage(artist.getMainImage())
                .build();
    }
}
