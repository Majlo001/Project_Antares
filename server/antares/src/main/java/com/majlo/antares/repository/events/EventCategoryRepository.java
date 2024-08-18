package com.majlo.antares.repository.events;

import com.majlo.antares.model.events.EventCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EventCategoryRepository extends JpaRepository<EventCategory, Long> {
}
