package com.majlo.antares.controller;

import com.majlo.antares.service.EmailService;
import jakarta.mail.MessagingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
public class EmailController {
    @Autowired
    private EmailService emailService;

    @GetMapping("/send-email")
    public String sendEmail() throws MessagingException {
        Map<String, Object> variables = new HashMap<>();
        variables.put("orderNumber", "123456");
        variables.put("orderDate", "2024-09-08");

        emailService.sendEmailWithTemplate("odbiorca@example.com", "Twoje zamówienie", variables);

        return "Email wysłany";
    }
}
