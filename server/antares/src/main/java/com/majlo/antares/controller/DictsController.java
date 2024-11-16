package com.majlo.antares.controller;

import com.auth0.jwt.exceptions.JWTVerificationException;
import com.majlo.antares.config.UserAuthenticationProvider;
import com.majlo.antares.dtos.UserDto;
import com.majlo.antares.dtos.dicts.*;
import com.majlo.antares.model.events.EventCategory;
import com.majlo.antares.model.events.EventStatus;
import com.majlo.antares.model.events.EventTag;
import com.majlo.antares.model.location.City;
import com.majlo.antares.model.location.Location;
import com.majlo.antares.model.location.LocationVariant;
import com.majlo.antares.repository.events.EventCategoryRepository;
import com.majlo.antares.repository.events.EventSeriesRepository;
import com.majlo.antares.repository.events.EventStatusRepository;
import com.majlo.antares.repository.events.EventTagRepository;
import com.majlo.antares.repository.location.CityRepository;
import com.majlo.antares.repository.location.LocationRepository;
import com.majlo.antares.repository.location.LocationVariantRepository;
import com.majlo.antares.service.AuthorizationService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/dicts")
public class DictsController {
    private final LocationRepository locationRepository;
    private final LocationVariantRepository locationVariantRepository;
    private final EventStatusRepository eventStatusRepository;
    private final EventSeriesRepository eventSeriesRepository;
    private final UserAuthenticationProvider userAuthenticationProvider;
    private final AuthorizationService authorizationService;
    private final CityRepository cityRepository;
    private final EventCategoryRepository eventCategoryRepository;
    private final EventTagRepository eventTagRepository;

    public DictsController(LocationRepository locationRepository, LocationVariantRepository locationVariantRepository, EventStatusRepository eventStatusRepository, EventSeriesRepository eventSeriesRepository, UserAuthenticationProvider userAuthenticationProvider, AuthorizationService authorizationService, CityRepository cityRepository, EventCategoryRepository eventCategoryRepository, EventTagRepository eventTagRepository) {
        this.locationRepository = locationRepository;
        this.locationVariantRepository = locationVariantRepository;
        this.eventStatusRepository = eventStatusRepository;
        this.eventSeriesRepository = eventSeriesRepository;
        this.userAuthenticationProvider = userAuthenticationProvider;
        this.authorizationService = authorizationService;
        this.cityRepository = cityRepository;
        this.eventCategoryRepository = eventCategoryRepository;
        this.eventTagRepository = eventTagRepository;
    }


    @GetMapping("/locations")
    public List<DictDto> getSimpleLocations(@RequestParam String query) {
        List<Location> locations = locationRepository.findAll();

        return locations.stream()
                .filter(location -> location.getName().toLowerCase().contains(query.toLowerCase()))
                .map(DictDto::fromLocation)
                .collect(Collectors.toList());
    }

    @GetMapping("/location_variants")
    public List<DictDto> getSimpleLocationVariants(@RequestParam Long id) {
        List<LocationVariant> locationVariants = locationRepository.findById(id).get().getLocationVariantList();

        return locationVariants.stream()
                .map(DictDto::fromLocationVariant)
                .collect(Collectors.toList());
    }

    @GetMapping("/event_statuses")
    public List<DictDto> getStatuses() {
        List<EventStatus> statuses = eventStatusRepository.findAll();

        return statuses.stream()
                .map(DictDto::fromEventStatus)
                .collect(Collectors.toList());
    }

    @GetMapping("/event_series")
    public List<DictDto> getEventSeries(@RequestHeader("Authorization") String authHeader) {
        Long userId = authorizationService.getAuthenticatedUserId(authHeader);

        return eventSeriesRepository.findAll().stream()
                .filter(eventSeries -> {
                    EventStatus status = eventSeries.getEventSeriesStatus();
                    String statusName = (status != null) ? status.getEventStatusName() : null;

                    boolean isNotCancelledOrFinished = !"Cancelled".equals(statusName) && !"Finished".equals(statusName);
                    boolean isCreatedByUser = eventSeries.getEventOwner() != null && eventSeries.getEventOwner().getId().equals(userId);

                    return isNotCancelledOrFinished && isCreatedByUser;
                })
                .map(DictDto::fromEventSeries)
                .collect(Collectors.toList());
    }

    @GetMapping("/location_cities")
    public List<DictDto> getLocationCities() {
        List<City> cities = cityRepository.findAll();

        return cities.stream()
                .map(DictDto::fromCity)
                .collect(Collectors.toList());
    }

    @GetMapping("/event_categories")
    public List<DictDto> getEventCategories() {
        List<EventCategory> eventCategories = eventCategoryRepository.findAll();

        return eventCategories.stream()
                .map(DictDto::fromEventCategory)
                .collect(Collectors.toList());
    }

    @GetMapping("/event_tags")
    public List<DictDto> getEventTags(@RequestParam String query) {
        List<EventTag> eventTags = eventTagRepository.findAll();

        return eventTags.stream()
                .filter(eventTag -> eventTag.getTagName().toLowerCase().contains(query.toLowerCase()))
                .map(DictDto::fromEventTag)
                .collect(Collectors.toList());
    }
}
