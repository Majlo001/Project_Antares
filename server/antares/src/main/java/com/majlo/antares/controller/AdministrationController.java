package com.majlo.antares.controller;

import com.majlo.antares.model.EventOwner;
import com.majlo.antares.repository.EventOwnerRepository;
import com.majlo.antares.repository.UserRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/admin")
public class AdministrationController {
    private final UserRepository userRepository;
    private final EventOwnerRepository eventOwnerRepository;

    public AdministrationController(UserRepository userRepository, EventOwnerRepository eventOwnerRepository) {
        this.userRepository = userRepository;
        this.eventOwnerRepository = eventOwnerRepository;
    }


    @PostMapping("/set_user_as_event_owner")
    public ResponseEntity<?> setUserAsEventOwner(@RequestParam Long userId) {
        if (!userRepository.existsById(userId)) {
            return ResponseEntity.notFound().build();
        }

        if (eventOwnerRepository.existsByEventOwner(userRepository.findById(userId).get())) {
            return ResponseEntity.badRequest().body("User is already an event owner");
        }

        EventOwner eventOwner = EventOwner.builder()
                .eventOwner(userRepository.findById(userId).get()).build();

        eventOwnerRepository.save(eventOwner);

        return ResponseEntity.ok().build();
    }
}
