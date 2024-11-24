package com.majlo.antares.controller;

import com.itextpdf.html2pdf.HtmlConverter;
import com.majlo.antares.dtos.tickets.UserTicketDto;
import com.majlo.antares.model.transaction.Ticket;
import com.majlo.antares.repository.transaction.TicketRepository;
import com.majlo.antares.service.AuthorizationService;
import com.majlo.antares.service.TicketService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.*;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;


import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/api/tickets")
public class TicketController {
    private final TicketService ticketService;
    private final TicketRepository ticketRepository;
    private final AuthorizationService authorizationService;

    public TicketController(TicketService ticketService, TicketRepository ticketRepository, AuthorizationService authorizationService) {
        this.ticketService = ticketService;
        this.ticketRepository = ticketRepository;
        this.authorizationService = authorizationService;
    }

    @GetMapping("/all")
    @Transactional
    public ResponseEntity<?> getAllTickets(@RequestHeader("Authorization") String authHeader) {
        try {
            Long userId = authorizationService.getAuthenticatedUserId(authHeader);

            List<UserTicketDto> userTickets = ticketRepository.findAllByTicketOwnerId(userId).stream()
                    .map(UserTicketDto::fromTicket)
                    .collect(Collectors.toList());
            return ResponseEntity.ok(userTickets);
        }
        catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().body("Error: " + e.getMessage());
        }
    }

    @GetMapping("/files/{filename}")
    public ResponseEntity<?> getTicket(@PathVariable String filename) {
        try {
            byte[] ticket = ticketService.getTicket(filename);

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_PDF);
            headers.setContentDisposition(ContentDisposition.inline().filename(filename).build());

            return ResponseEntity.ok()
                    .headers(headers)
                    .body(ticket);
        }
        catch (IOException e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

//    @GetMapping("/files/{filename}")
//    @Transactional
//    public ResponseEntity<?> getTicket(@RequestHeader("Authorization") String authHeader, @PathVariable String filename, @RequestParam Long ticket_id) {
//        try {
//            Long userId = authorizationService.getAuthenticatedUserId(authHeader);
//            Ticket ticket = ticketRepository.findById(ticket_id).orElse(null);
//
//            if (ticket == null || !ticket.getTicketOwner().getId().equals(userId)) {
//                return ResponseEntity.status(HttpStatus.FORBIDDEN).body("You are not authorized to access this file.");
//            }
//        }
//        catch (Exception e) {
//            e.printStackTrace();
//            return ResponseEntity.badRequest().body("Error: " + e.getMessage());
//        }
//
//        try {
//            byte[] ticket = ticketService.getTicket(filename);
//            System.out.println("Ticket byte array size: " + ticket.length);
//            System.out.println("First 100 bytes: " + Arrays.toString(Arrays.copyOf(ticket, 100)));
//
//
//            HttpHeaders headers = new HttpHeaders();
//            headers.setContentType(MediaType.APPLICATION_PDF);
//            headers.setContentDisposition(ContentDisposition.inline().filename(filename).build());
//
//            return ResponseEntity.ok()
//                    .headers(headers)
//                    .body(ticket);
//        }
//        catch (IOException e) {
//            e.printStackTrace();
//            return ResponseEntity.notFound().build();
//        }
//    }


}
