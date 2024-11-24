package com.majlo.antares.repository.transaction;

import com.majlo.antares.model.User;
import com.majlo.antares.model.transaction.Ticket;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TicketRepository extends JpaRepository<Ticket, Long> {
    List<Ticket> findAllByTicketOwnerId(Long ticketOwnerId);

    Ticket findByTicketPdfLink(String ticketPdfLink);
}
