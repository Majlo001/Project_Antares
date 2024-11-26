package com.majlo.antares.controller;

import com.majlo.antares.dtos.locationDetail.LocationDto;
import com.majlo.antares.model.location.Location;
import com.majlo.antares.repository.location.LocationRepository;
import jakarta.transaction.Transactional;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/locations")
public class LocationController {
    private final LocationRepository locationRepository;

    public LocationController(LocationRepository locationRepository) {
        this.locationRepository = locationRepository;
    }


    @GetMapping
    @Transactional
    public ResponseEntity<?> getLocations(@RequestParam Long locationId, @RequestParam Integer maxEvents) {
        try {
            Location location = locationRepository.findById(locationId).orElseThrow();
            LocationDto locationDto = LocationDto.fromLocation(location, maxEvents);

            return ResponseEntity.ok(locationDto);
        }
        catch (Exception e) {
            return ResponseEntity.badRequest().body("Location not found");
        }
    }
}
