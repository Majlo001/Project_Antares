package com.majlo.antares.repository.location;

import com.majlo.antares.model.location.TicketPrice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TicketPriceRepository extends JpaRepository<TicketPrice, Long> {

}
