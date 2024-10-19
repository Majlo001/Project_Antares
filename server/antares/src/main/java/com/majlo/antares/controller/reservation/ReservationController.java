package com.majlo.antares.controller.reservation;

import com.majlo.antares.dtos.payment.PaymentRequestDto;
import com.majlo.antares.dtos.reservation.SeatReservationRequestDto;
import com.majlo.antares.model.transaction.TransactionEntity;
import com.majlo.antares.service.payment.PaymentService;
import com.majlo.antares.service.reservation.EventSeatStatusService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

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
    public ResponseEntity<String> reserveSeats(@RequestBody List<SeatReservationRequestDto> seatRequests, HttpServletRequest request) {
        HttpSession session = request.getSession();
        Long userId = (Long) session.getAttribute("userId");
        String sessionId;

        try {
            /** Check if user is logged in */
            if (userId == null) {
                sessionId = (String) session.getAttribute("sessionId");

                if (sessionId == null) {
                    /** Generate new session id */
                    sessionId = UUID.randomUUID().toString();
                    session.setAttribute("sessionId", sessionId);
                }
            }
            else {
                sessionId = null;
            }

            eventSeatStatusService.reserveSeats(seatRequests, userId, sessionId);
            return ResponseEntity.ok("Seats reserved successfully");
        }
        catch (RuntimeException e) {
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
