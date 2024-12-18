package com.majlo.antares.repository.events;

import com.majlo.antares.model.Artist;
import com.majlo.antares.model.events.EventSeries;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface EventSeriesRepository extends JpaRepository<EventSeries, Long> {
    List<EventSeries> findAllByArtistsContains(Artist artist);
}
