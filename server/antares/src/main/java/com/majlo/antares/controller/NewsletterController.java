package com.majlo.antares.controller;

import com.majlo.antares.service.NewsletterService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/newsletter")
public class NewsletterController {
    private final NewsletterService newsletterService;

    public NewsletterController(NewsletterService newsletterService) {
        this.newsletterService = newsletterService;
    }

    @PostMapping("/subscribe")
    public ResponseEntity<?> subscribe(String email) {
        if (newsletterService.addSubscriber(email)) {
            return ResponseEntity.ok().build();
        }

        return ResponseEntity.status(HttpStatus.CONFLICT)
                .body("This email is already subscribed.");
    }

    @DeleteMapping("/unsubscribe")
    public ResponseEntity<?> unsubscribe(String email) {
        newsletterService.removeSubscriber(email);
        return ResponseEntity.ok().build();
    }
}
