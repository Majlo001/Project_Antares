package com.majlo.antares.controller;

import com.beust.ah.A;
import com.majlo.antares.dtos.atristDetail.ArtistDto;
import com.majlo.antares.model.Artist;
import com.majlo.antares.repository.events.ArtistRepostiory;
import com.majlo.antares.repository.events.EventSeriesRepository;
import jakarta.transaction.Transactional;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/artists")
public class ArtistController {

    private final ArtistRepostiory artistRepostiory;
    private final EventSeriesRepository eventSeriesRepository;

    public ArtistController(ArtistRepostiory artistRepostiory, EventSeriesRepository eventSeriesRepository) {
        this.artistRepostiory = artistRepostiory;
        this.eventSeriesRepository = eventSeriesRepository;
    }

    @GetMapping
    @Transactional
    public ResponseEntity<?> getArtists(@RequestParam Long artistId, @RequestParam Integer maxEvents) {
        try {
            Artist artist = artistRepostiory.findById(artistId).orElseThrow();
            ArtistDto artistDto = ArtistDto.fromArtist(artist, eventSeriesRepository, maxEvents);

            return ResponseEntity.ok(artistDto);
        }
        catch (Exception e) {
            return ResponseEntity.badRequest().body("Artist not found");
        }
    }

}
