package com.majlo.antares.controller.payment;

import com.majlo.antares.dtos.payment.PaymentRequestDto;
import com.majlo.antares.model.User;
import com.majlo.antares.model.transaction.TransactionEntity;
import com.majlo.antares.service.AuthorizationService;
import com.majlo.antares.service.payment.PaymentService;
import com.majlo.antares.service.payment.PaypalService;
import com.paypal.api.payments.Links;
import com.paypal.api.payments.Payment;
import com.paypal.base.rest.PayPalRESTException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/api/payment")
public class PaypalController {
    private final PaypalService paypalService;
    private final AuthorizationService authorizationService;
    private final PaymentService paymentService;

    @Value("${frontend.base.url}")
    private String frontendBaseUrl;

    @PostMapping("/create")
    public ResponseEntity<Map<String, String>> createPayment(
            @RequestParam("method") String method,
            @RequestParam("amount") String amount,
            @RequestParam("currency") String currency,
            @RequestParam("description") String description
    ) {
        try {
            String cancelUrl =  frontendBaseUrl + "/payment/cancel";
            String successUrl = frontendBaseUrl + "/payment/success";
            Payment payment = paypalService.createPayment(
                    Double.valueOf(amount),
                    currency,
                    method,
                    "sale",
                    description,
                    cancelUrl,
                    successUrl
            );

            for (Links links: payment.getLinks()) {
                if (links.getRel().equals("approval_url")) {
                    Map<String, String> response = new HashMap<>();
                    response.put("approvalUrl", links.getHref());
                    return ResponseEntity.ok(response);
                }
            }
        } catch (PayPalRESTException e) {
            log.error("Error occurred:: ", e);
            return ResponseEntity.status(500).body(Map.of("error", "Payment creation failed"));
        }
        return ResponseEntity.status(500).body(Map.of("error", "Payment approval URL not found"));
    }

    @PostMapping("/success")
    public ResponseEntity<Map<String, String>> paymentSuccess(
            @RequestHeader("Authorization") String authHeader,
            @RequestParam("paymentId") String paymentId,
            @RequestParam("payerId") String payerId,
            @RequestBody PaymentRequestDto paymentRequest
    ) {
        try {
            Payment payment = paypalService.executePayment(paymentId, payerId);
            Map<String, String> response = new HashMap<>();

            if ("approved".equals(payment.getState())) {
                User user = authorizationService.getAuthenticatedUser(authHeader);

                try {
                    TransactionEntity transactionEntity = paymentService.payForMultipleSeats(paymentRequest.getSeatReservations(), user, paymentRequest.getPaymentMethod(), paymentRequest.getDiscountCode());
                    response.put("status", "Payment approved");
                    return ResponseEntity.ok(response);
                }
                catch (RuntimeException e) {
                    response.put("status", "Payment not approved");
                    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
                }
                catch (Exception e) {
                    response.put("status", "Payment not approved");
                    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
                }

            }

            response.put("status", "Payment not approved");
            return ResponseEntity.status(400).body(response);
        }
        catch (PayPalRESTException e) {
            log.error("Error occurred:: ", e);
            return ResponseEntity.status(500).body(Map.of("error", "Payment execution failed"));
        }
    }

    @GetMapping("/cancel")
    public ResponseEntity<Map<String, String>> paymentCancel() {
        return ResponseEntity.status(400).body(Map.of("status", "Payment cancelled"));
    }

    @GetMapping("/error")
    public ResponseEntity<Map<String, String>> paymentError() {
        return ResponseEntity.status(500).body(Map.of("error", "Payment error"));
    }
}
