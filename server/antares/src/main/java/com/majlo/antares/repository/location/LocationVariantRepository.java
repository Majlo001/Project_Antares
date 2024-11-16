package com.majlo.antares.repository.location;

import aj.org.objectweb.asm.commons.Remapper;
import com.majlo.antares.model.location.LocationVariant;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LocationVariantRepository extends JpaRepository<LocationVariant, Long> {
}
