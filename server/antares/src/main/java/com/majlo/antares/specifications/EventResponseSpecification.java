package com.majlo.antares.specifications;

import com.majlo.antares.model.events.Event;
import com.majlo.antares.model.location.Location;
import org.springframework.data.jpa.domain.Specification;
import jakarta.persistence.criteria.Predicate;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class EventResponseSpecification {

    public static Specification<Event> filterByCriteria(Location location, LocalDateTime dateStart, LocalDateTime dateEnd, String tag) {
        return (root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();

            if (location != null) {
                predicates.add(criteriaBuilder.equal(root.get("location"), location));
            }

            if (dateStart != null && dateEnd != null) {
                predicates.add(criteriaBuilder.between(root.get("eventDateStart"), dateStart, dateEnd));
            }

            if (tag != null) {
                predicates.add(criteriaBuilder.isMember(tag, root.get("tags")));
            }

            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };
    }
}
