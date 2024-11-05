package com.majlo.antares.controller;

import com.auth0.jwt.exceptions.JWTVerificationException;
import com.majlo.antares.config.UserAuthenticationProvider;
import com.majlo.antares.dtos.UserDto;
import com.majlo.antares.dtos.dicts.EventSeriesDictDto;
import com.majlo.antares.dtos.dicts.EventStatusDictDto;
import com.majlo.antares.dtos.dicts.LocationDictDto;
import com.majlo.antares.dtos.dicts.LocationVariantDictDto;
import com.majlo.antares.model.events.EventStatus;
import com.majlo.antares.model.location.Location;
import com.majlo.antares.model.location.LocationVariant;
import com.majlo.antares.repository.events.EventSeriesRepository;
import com.majlo.antares.repository.events.EventStatusRepository;
import com.majlo.antares.repository.location.LocationRepository;
import com.majlo.antares.repository.location.LocationVariantRepository;
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

    public DictsController(LocationRepository locationRepository, LocationVariantRepository locationVariantRepository, EventStatusRepository eventStatusRepository, EventSeriesRepository eventSeriesRepository, UserAuthenticationProvider userAuthenticationProvider) {
        this.locationRepository = locationRepository;
        this.locationVariantRepository = locationVariantRepository;
        this.eventStatusRepository = eventStatusRepository;
        this.eventSeriesRepository = eventSeriesRepository;
        this.userAuthenticationProvider = userAuthenticationProvider;
    }


    @GetMapping("/locations")
    public List<LocationDictDto> getSimpleLocations(@RequestParam String query) {
        List<Location> locations = locationRepository.findAll();

        return locations.stream()
                .filter(location -> location.getName().toLowerCase().contains(query.toLowerCase()))
                .map(LocationDictDto::fromLocation)
                .collect(Collectors.toList());
    }

    @GetMapping("/location_variants")
    public List<LocationVariantDictDto> getSimpleLocationVariants(@RequestParam Long id) {
        List<LocationVariant> locationVariants = locationRepository.findById(id).get().getLocationVariantList();

        return locationVariants.stream()
                .map(LocationVariantDictDto::fromLocationVariant)
                .collect(Collectors.toList());
    }

    @GetMapping("/event_statuses")
    public List<EventStatusDictDto> getStatuses() {
        List<EventStatus> statuses = eventStatusRepository.findAll();

        return statuses.stream()
                .map(EventStatusDictDto::fromEventStatus)
                .collect(Collectors.toList());
    }

    @GetMapping("/event_series")
    public List<EventSeriesDictDto> getEventSeries(@RequestHeader("Authorization") String authHeader) {
        Long userId;

        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            String token = authHeader.substring(7);
            Authentication authentication = userAuthenticationProvider.validateToken(token);
            userId = ((UserDto) authentication.getPrincipal()).getId();
        }
        else {
            userId = null;
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Authorization token is missing or invalid");
        }


        return eventSeriesRepository.findAll().stream()
//                .filter(eventSeries -> {
//                    String statusName = eventSeries.getEventSeriesStatus().getEventStatusName();
//                    boolean isIncorrectStatus = "Cancelled".equals(statusName) || "Finished".equals(statusName);
//                    boolean isCreatedByUser = eventSeries.getEventOwner().getId().equals(userId);
//                    return !isIncorrectStatus || isCreatedByUser;
//                })
//                .map(EventSeriesDictDto::fromEventSeries)
//                .collect(Collectors.toList());
                .filter(eventSeries -> {
                    EventStatus status = eventSeries.getEventSeriesStatus();
                    String statusName = (status != null) ? status.getEventStatusName() : null;

                    boolean isNotCancelledOrFinished = !"Cancelled".equals(statusName) && !"Finished".equals(statusName);
                    boolean isCreatedByUser = eventSeries.getEventOwner() != null && eventSeries.getEventOwner().getId().equals(userId);

                    return isNotCancelledOrFinished && isCreatedByUser;
                })
                .map(EventSeriesDictDto::fromEventSeries)
                .collect(Collectors.toList());
    }
}
