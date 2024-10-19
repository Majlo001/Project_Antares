package com.majlo.antares.repository.location;

import com.majlo.antares.model.location.Row;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RowRepository extends JpaRepository<Row, Long> {
}
