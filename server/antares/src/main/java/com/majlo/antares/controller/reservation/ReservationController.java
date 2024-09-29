package com.majlo.antares.controller.reservation;

import com.majlo.antares.dtos.payment.PaymentRequestDto;
import com.majlo.antares.dtos.reservation.SeatReservationRequestDto;
import com.majlo.antares.model.transaction.TransactionEntity;
import com.majlo.antares.service.payment.PaymentService;
import com.majlo.antares.service.reservation.EventSeatStatusService;
import jakarta.servlet.http.HttpSession;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/reservation")
public class ReservationController {
    private final EventSeatStatusService eventSeatStatusService;
    private final PaymentService paymentService;

    public ReservationController(EventSeatStatusService eventSeatStatusService, PaymentService paymentService) {
        this.eventSeatStatusService = eventSeatStatusService;
        this.paymentService = paymentService;
    }

    /** Seat reservation */
    @PostMapping("/reserve")
    public ResponseEntity<String> reserveSeats(@RequestBody List<SeatReservationRequestDto> seatRequests, HttpSession session) {
        Long userId = (Long) session.getAttribute("userId");
        if (userId == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("User not logged in");
        }

        try {
            eventSeatStatusService.reserveSeats(seatRequests, userId);
            return ResponseEntity.ok("Seats reserved successfully");
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    /** Payment for reserved seats */
    @PostMapping("/pay")
    public ResponseEntity<?> payForSeats(@RequestBody PaymentRequestDto paymentRequest, HttpSession session) {
        Long userId = (Long) session.getAttribute("userId");
        if (userId == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("User not logged in");
        }

        try {
            TransactionEntity transactionEntity = paymentService.payForMultipleSeats(paymentRequest.getSeatRequests(), userId, paymentRequest.getPaymentMethod(), paymentRequest.getDiscountCode());
            return ResponseEntity.ok(transactionEntity);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }
}
