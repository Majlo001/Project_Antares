package com.majlo.antares.repository.events;

import com.majlo.antares.model.events.EventSeries;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EventSeriesRepository extends JpaRepository<EventSeries, Long> {
}
