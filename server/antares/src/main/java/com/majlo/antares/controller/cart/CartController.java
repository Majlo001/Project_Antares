package com.majlo.antares.controller.cart;

import com.majlo.antares.dtos.cart.CartEventDataDto;
import com.majlo.antares.model.events.Event;
import com.majlo.antares.repository.events.EventRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/cart_data")
public class CartController {

    private final EventRepository eventRepository;

    @GetMapping("/tickets_info")
    public ResponseEntity<?> getTicketsInfo(
            @RequestParam Long eventId,
            @RequestParam List<Long> sectorIds) {

        try {
            Event event = eventRepository.findById(eventId).orElseThrow();
            CartEventDataDto cartEventDataDto = CartEventDataDto.fromEvent(event, sectorIds);
            return ResponseEntity.ok(cartEventDataDto);
        }
        catch (Exception e) {
            return ResponseEntity.badRequest().body("Error while getting cart data");
        }
    }
}
