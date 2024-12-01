package com.majlo.antares.repository.events;

import com.majlo.antares.model.events.EventTag;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.ServiceLoader;

@Repository
public interface EventTagRepository extends JpaRepository<EventTag, Long> {
    EventTag findByTagName(String tagName);
}
