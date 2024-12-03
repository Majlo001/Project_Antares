package com.majlo.antares.repository.location;

import com.majlo.antares.model.events.Event;
import com.majlo.antares.model.location.Sector;
import com.majlo.antares.model.location.TicketPrice;
import com.majlo.antares.model.location.TicketType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.lang.ScopedValue;
import java.util.Optional;

@Repository
public interface TicketPriceRepository extends JpaRepository<TicketPrice, Long> {
    Optional<TicketPrice> findBySectorAndEventAndTicketType(Sector sector, Event event, TicketType ticketType);
}
