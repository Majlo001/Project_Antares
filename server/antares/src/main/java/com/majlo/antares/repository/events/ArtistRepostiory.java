package com.majlo.antares.repository.events;

import com.majlo.antares.model.Artist;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ArtistRepostiory  extends JpaRepository<Artist, Long> {
}
