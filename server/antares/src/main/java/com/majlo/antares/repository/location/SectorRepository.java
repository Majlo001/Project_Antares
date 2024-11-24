package com.majlo.antares.repository.location;

import com.majlo.antares.model.location.LocationVariant;
import com.majlo.antares.model.location.Sector;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SectorRepository extends JpaRepository<Sector, Long> {

    List<Sector> findAllByLocationVariant(LocationVariant locationVariant);
}
