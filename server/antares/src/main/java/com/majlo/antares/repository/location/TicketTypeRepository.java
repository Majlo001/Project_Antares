package com.majlo.antares.repository.location;

import com.majlo.antares.model.location.TicketType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TicketTypeRepository extends JpaRepository<TicketType, Long> {

    TicketType findByName(String normal);
}
