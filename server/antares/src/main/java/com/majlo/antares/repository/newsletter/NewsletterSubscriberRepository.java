package com.majlo.antares.repository.newsletter;

import com.majlo.antares.model.newsletter.NewsletterSubscriber;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface NewsletterSubscriberRepository extends JpaRepository<NewsletterSubscriber, Long> {
    NewsletterSubscriber findByEmail(String email);
}
