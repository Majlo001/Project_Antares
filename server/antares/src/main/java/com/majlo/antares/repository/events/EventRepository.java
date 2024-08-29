package com.majlo.antares.repository.events;

import com.majlo.antares.model.events.Event;
import com.majlo.antares.model.location.Location;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;

@Repository
public interface EventRepository extends JpaRepository<Event, Long> {

    Page<Event> findAll(Specification<Event> specification, Pageable pageable);
}
