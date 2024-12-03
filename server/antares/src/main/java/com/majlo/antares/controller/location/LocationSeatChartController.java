package com.majlo.antares.controller.location;

import com.majlo.antares.dtos.locationSeatChart.LocationSeatChartDto;
import com.majlo.antares.dtos.locationSeatChartAvailability.LocationSeatChartAvailabilityDto;
import com.majlo.antares.model.events.Event;
import com.majlo.antares.model.location.LocationVariant;
import com.majlo.antares.model.reservation.EventSeatStatus;
import com.majlo.antares.repository.events.EventRepository;
import com.majlo.antares.repository.location.LocationRepository;
import com.majlo.antares.repository.location.LocationVariantRepository;
import jakarta.transaction.Transactional;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/location_seat_chart")
public class LocationSeatChartController {
    private final LocationVariantRepository locationVariantRepository;
    private final LocationRepository locationRepository;
    private final EventRepository eventRepository;

    public LocationSeatChartController(LocationVariantRepository locationVariantRepository, LocationRepository locationRepository, EventRepository eventRepository) {
        this.locationVariantRepository = locationVariantRepository;
        this.locationRepository = locationRepository;
        this.eventRepository = eventRepository;
    }

    @GetMapping("/get_chart")
    @Transactional
    public ResponseEntity<?> getChart(@RequestParam Long eventId) {
        try {
            LocationVariant locationVariant = eventRepository.findById(eventId).get().getLocationVariant();
            Event event = eventRepository.findById(eventId)
                    .orElseThrow(() -> new IllegalArgumentException("Event not found"));

            LocationSeatChartDto locationSeatChartDto = LocationSeatChartDto.fromLocationVariant(
                    locationVariant,
                    event);
            return ResponseEntity.ok(locationSeatChartDto);
        }
        catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().body("Event with id " + eventId + " not found " + e.getMessage());
        }
    }

    @GetMapping("/get_available_seats")
    @Transactional
    public ResponseEntity<?> getAvailableSeats(@RequestParam Long eventId) {
        try {
            List<EventSeatStatus> eventSeatStatuses = eventRepository.findById(eventId).get().getEventSeatStatuses();
            System.out.println("Count of eventSeatStatuses: " + eventSeatStatuses.size());

            List<LocationSeatChartAvailabilityDto> availabilityDtos = eventSeatStatuses.stream()
                    .filter(eventSeatStatus -> eventSeatStatus.getSeat() != null)
                    .map(LocationSeatChartAvailabilityDto::fromEventSeatStatus)
                    .collect(Collectors.toList());

            return ResponseEntity.ok(availabilityDtos);
        }
        catch (Exception e) {
            return ResponseEntity.badRequest().body("Event with id " + eventId + " not found " + e.getMessage());
        }
    }

    @GetMapping("/get_available_sector_seats")
    public ResponseEntity<?> getAvailableSectorSeats(@RequestParam Long eventId, @RequestParam Long sectorId) {
        try {
            List<EventSeatStatus> eventSeatStatuses = eventRepository.findById(eventId).get().getEventSeatStatuses();


            if (sectorId != null) {
                eventSeatStatuses = eventSeatStatuses.stream()
                        .filter(eventSeatStatus -> eventSeatStatus.getSector().getId().equals(sectorId))
                        .toList();
            }

            List<LocationSeatChartAvailabilityDto> availabilityDtos = eventSeatStatuses.stream()
                    .map(LocationSeatChartAvailabilityDto::fromEventSeatStatus)
                    .collect(Collectors.toList());

            return ResponseEntity.ok(availabilityDtos);

        }
        catch (Exception e) {
            return ResponseEntity.badRequest().body("Event with id " + eventId + " not found " + e.getMessage());
        }
    }

}
