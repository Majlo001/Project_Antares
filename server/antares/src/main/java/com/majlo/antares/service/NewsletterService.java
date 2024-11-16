package com.majlo.antares.service;

import com.majlo.antares.model.newsletter.NewsletterSubscriber;
import com.majlo.antares.repository.newsletter.NewsletterSubscriberRepository;
import jakarta.mail.MessagingException;
import org.springframework.stereotype.Service;

@Service
public class NewsletterService {
    private final NewsletterSubscriberRepository newsletterSubscriberRepository;
    private final EmailService emailService;

    public NewsletterService(NewsletterSubscriberRepository newsletterSubscriberRepository, EmailService emailService) {
        this.newsletterSubscriberRepository = newsletterSubscriberRepository;
        this.emailService = emailService;
    }

    public Boolean addSubscriber(String email) {
        if (newsletterSubscriberRepository.findByEmail(email) != null) {
            return false;
        }

        NewsletterSubscriber newsletterSubscriber = new NewsletterSubscriber();
        newsletterSubscriber.setEmail(email);
        newsletterSubscriberRepository.save(newsletterSubscriber);

        try {
            emailService.sendNewsletterWelcomeEmail(email);
        } catch (MessagingException e) {
            e.printStackTrace();
        }

        return true;
    }

    public void removeSubscriber(String email) {
        NewsletterSubscriber subscriber = newsletterSubscriberRepository.findByEmail(email);
        newsletterSubscriberRepository.delete(subscriber);
    }
}
