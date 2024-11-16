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
import com.majlo.antares.exceptions.SeatLimitExceededException;
import com.majlo.antares.model.transaction.TransactionEntity;
import com.majlo.antares.service.AuthorizationService;
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
@RequestMapping("/api/reservation")
public class ReservationController {
    private final EventSeatStatusService eventSeatStatusService;
    private final PaymentService paymentService;
    private final UserAuthenticationProvider userAuthenticationProvider;
    private final AuthorizationService authorizationService;

    public ReservationController(EventSeatStatusService eventSeatStatusService, PaymentService paymentService, UserAuthenticationProvider userAuthenticationProvider, AuthorizationService authorizationService) {
        this.eventSeatStatusService = eventSeatStatusService;
        this.paymentService = paymentService;
        this.userAuthenticationProvider = userAuthenticationProvider;
        this.authorizationService = authorizationService;
    }

    @Value("${security.jwt.token.secret-key}")
    private String jwtSecret;

    /** Seat reservation */
    @PostMapping("/reserve")
    public ResponseEntity<?> reserveSeats(
            @RequestHeader(value = "Authorization", required = false) String authHeader,
            @RequestBody List<SeatReservationRequestDto> seatRequests,
            HttpServletRequest request) {

        HttpSession session = request.getSession();
        String sessionId;

        try {
            /** Token validation */
            Long userId = authorizationService.getAuthenticatedUserId(authHeader);

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


            List<Long> reservedSeatIds = null;
            try {
                reservedSeatIds = eventSeatStatusService.reserveSeats(seatRequests, userId, sessionId);
            }
            catch (SeatLimitExceededException e) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
            }


            return ResponseEntity.ok(reservedSeatIds);
        }
        catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    @PostMapping("/unreserve")
    public ResponseEntity<?> unreserveSeats(
            @RequestHeader(value = "Authorization", required = false) String authHeader,
            @RequestBody List<Long> reservedSeatIds,
            HttpServletRequest request) {

        Long userId = null;
        try {
            userId = authorizationService.getAuthenticatedUserId(authHeader);
        }
        catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }

        eventSeatStatusService.unreserveSeats(reservedSeatIds, userId);
        return ResponseEntity.ok("Seats unreserved successfully");
    }


    /** Payment for reserved seats */
    @PostMapping("/pay")
    public ResponseEntity<?> payForSeats(
            @RequestHeader("Authorization") String authHeader,
            @RequestBody PaymentRequestDto paymentRequest,
            HttpSession session) {

        Long userId = authorizationService.getAuthenticatedUserId(authHeader);

        try {
            TransactionEntity transactionEntity = paymentService.payForMultipleSeats(paymentRequest.getSeatReservations(), userId, paymentRequest.getPaymentMethod(), paymentRequest.getDiscountCode());
            return ResponseEntity.ok(transactionEntity);
        }
        catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }
}
