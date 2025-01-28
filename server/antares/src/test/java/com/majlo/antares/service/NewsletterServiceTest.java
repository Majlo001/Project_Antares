package com.majlo.antares.service;

import com.majlo.antares.model.newsletter.NewsletterSubscriber;
import com.majlo.antares.repository.newsletter.NewsletterSubscriberRepository;
import jakarta.mail.MessagingException;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;


@SpringBootTest
public class NewsletterServiceTest {

    @Test
    void contextLoads() {
    }

    @Test
    void testExample() {
        String greeting = "Hello, Spring!";
        assertEquals("Hello, Spring!", greeting);
    }

}
