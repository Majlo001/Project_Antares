package com.majlo.antares.repository.events;

import com.majlo.antares.model.events.EventTag;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EventTagRepository extends JpaRepository<EventTag, Long> {
}
