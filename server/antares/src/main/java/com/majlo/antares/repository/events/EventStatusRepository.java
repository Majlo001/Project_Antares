package com.majlo.antares.repository.events;

import com.majlo.antares.model.events.EventStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EventStatusRepository extends JpaRepository<EventStatus, Long> {
}
