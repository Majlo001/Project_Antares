package com.majlo.antares.controller;

import com.majlo.antares.enums.MainRole;
import com.majlo.antares.model.EventOwner;
import com.majlo.antares.model.User;
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

        User user = userRepository.findById(userId).get();
        if (eventOwnerRepository.existsByEventOwner(user)) {
            return ResponseEntity.badRequest().body("User is already an event owner");
        }

        EventOwner eventOwner = EventOwner.builder()
                .eventOwner(user).build();

        user.setRole(MainRole.valueOf("EVENT_OWNER"));
        userRepository.save(user);

        eventOwner.setName("Event Company XYZ");
        eventOwner.setImage("/api/images/files/ba37662d-86ed-4cea-a9c6-a33b7fe5fdbb.jpg");
        eventOwnerRepository.save(eventOwner);

        return ResponseEntity.ok().build();
    }

    @PostMapping("/set_ticket_controller")
    public ResponseEntity<?> setTicketController(@RequestParam Long userId) {
        if (!userRepository.existsById(userId)) {
            return ResponseEntity.notFound().build();
        }

        User user = userRepository.findById(userId).get();
        user.setRole(MainRole.valueOf("TICKET_CONTROLLER"));
        userRepository.save(user);
        return ResponseEntity.ok().build();
    }


    @PostMapping("/set_admin")
    public ResponseEntity<?> setAdmin(@RequestParam Long userId) {
        if (!userRepository.existsById(userId)) {
            return ResponseEntity.notFound().build();
        }

        User user = userRepository.findById(userId).get();
        user.setRole(MainRole.valueOf("ADMIN"));
        userRepository.save(user);
        return ResponseEntity.ok().build();
    }

}
