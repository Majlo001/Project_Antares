package com.majlo.antares.specifications;

import com.majlo.antares.model.events.Event;
import com.majlo.antares.model.events.EventSeries;
import com.majlo.antares.model.location.City;
import jakarta.persistence.criteria.Expression;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.JoinType;
import org.springframework.data.jpa.domain.Specification;
import jakarta.persistence.criteria.Predicate;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class EventResponseSpecification {

    public static Specification<Event> filterByCriteria(City city, LocalDateTime dateStart, LocalDateTime dateEnd, String category, String searchText) {
        return (root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();

            if (city != null) {
                predicates.add(criteriaBuilder.equal(root.get("location").get("city"), city));            }

            if (dateStart != null && dateEnd != null) {
                Expression<LocalDate> eventDateStart = criteriaBuilder.function("date", LocalDate.class, root.get("eventDateStart"));

                predicates.add(criteriaBuilder.between(eventDateStart, dateStart.toLocalDate(), dateEnd.toLocalDate()));
            }

            if (category != null && !category.isEmpty())  {
                Join<Event, EventSeries> eventSeriesJoin = root.join("eventSeries", JoinType.LEFT);
                predicates.add(criteriaBuilder.equal(eventSeriesJoin.get("category").get("eventCategoryName"), category));
            }

            if (searchText != null) {
                predicates.add(criteriaBuilder.like(criteriaBuilder.lower(root.get("name")), "%" + searchText.toLowerCase() + "%"));
            }

            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };
    }

    public static Specification<Event> filterByCriteria(City city, LocalDateTime dateStart, LocalDateTime dateEnd, String category, String status, Boolean isPublic, String searchText) {
        return (root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();

            if (city != null) {
                predicates.add(criteriaBuilder.equal(root.get("location").get("city"), city));            }

            if (dateStart != null && dateEnd != null) {
                Expression<LocalDate> eventDateStart = criteriaBuilder.function("date", LocalDate.class, root.get("eventDateStart"));

                predicates.add(criteriaBuilder.between(eventDateStart, dateStart.toLocalDate(), dateEnd.toLocalDate()));
            }

            if (category != null && !category.isEmpty())  {
                Join<Event, EventSeries> eventSeriesJoin = root.join("eventSeries", JoinType.LEFT);
                predicates.add(criteriaBuilder.equal(eventSeriesJoin.get("category").get("eventCategoryName"), category));
            }

            if (status != null && !status.isEmpty()) {
                predicates.add(criteriaBuilder.equal(root.get("status").get("eventStatusName"), status));
            }

            if (isPublic != null) {
                predicates.add(criteriaBuilder.equal(root.get("isPublic"), isPublic));
            }

            if (searchText != null) {
                predicates.add(criteriaBuilder.like(criteriaBuilder.lower(root.get("name")), "%" + searchText.toLowerCase() + "%"));
            }

            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };
    }

    public static Specification<Event> filterByEventName(String eventName) {
        return (root, query, criteriaBuilder) -> {
            if (eventName == null || eventName.isEmpty()) {
                return criteriaBuilder.conjunction();
            }
            return criteriaBuilder.like(criteriaBuilder.lower(root.get("name")), "%" + eventName.toLowerCase() + "%");
        };
    }
}
