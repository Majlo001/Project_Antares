package com.majlo.antares.controller.reservation;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.majlo.antares.config.UserAuthenticationProvider;
import com.majlo.antares.dtos.UserDto;
import com.majlo.antares.dtos.payment.PaymentRequestDto;
import com.majlo.antares.dtos.reservation.SeatReservationRequestDto;
import com.majlo.antares.model.transaction.TransactionEntity;
import com.majlo.antares.service.payment.PaymentService;
import com.majlo.antares.service.reservation.EventSeatStatusService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.RequestHeader;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/reservation")
public class ReservationController {
    private final EventSeatStatusService eventSeatStatusService;
    private final PaymentService paymentService;
    private final UserAuthenticationProvider userAuthenticationProvider;

    public ReservationController(EventSeatStatusService eventSeatStatusService, PaymentService paymentService, UserAuthenticationProvider userAuthenticationProvider) {
        this.eventSeatStatusService = eventSeatStatusService;
        this.paymentService = paymentService;
        this.userAuthenticationProvider = userAuthenticationProvider;
    }

    @Value("${security.jwt.token.secret-key}")
    private String jwtSecret;

    /** Seat reservation */
    @PostMapping("/reserve")
    public ResponseEntity<String> reserveSeats(
            @RequestHeader(value = "Authorization", required = false) String authHeader,
            @RequestBody List<SeatReservationRequestDto> seatRequests,
            HttpServletRequest request) {

        HttpSession session = request.getSession();
        Long userId = null;
        String sessionId;

        try {
            /** Token validation */
            if (authHeader != null && authHeader.startsWith("Bearer ")) {
                String token = authHeader.substring(7);

                Authentication authentication = userAuthenticationProvider.validateToken(token);
                userId = ((UserDto) authentication.getPrincipal()).getId();
            }

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
    public ResponseEntity<?> payForSeats(
            @RequestHeader("Authorization") String authHeader,
            @RequestBody PaymentRequestDto paymentRequest,
            HttpSession session) {

        Long userId = null;

        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            String token = authHeader.substring(7);

            try {
                Authentication authentication = userAuthenticationProvider.validateToken(token);
                userId = ((UserDto) authentication.getPrincipal()).getId();
            }
            catch (JWTVerificationException e) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid token");
            }
        }

        try {
            TransactionEntity transactionEntity = paymentService.payForMultipleSeats(paymentRequest.getSeatRequests(), userId, paymentRequest.getPaymentMethod(), paymentRequest.getDiscountCode());
            return ResponseEntity.ok(transactionEntity);
        }
        catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }
}
