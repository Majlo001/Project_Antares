package com.majlo.antares.repository.location;

import com.majlo.antares.model.location.City;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface CityRepository extends JpaRepository<City, Long> {
    City findByCityName(String cityName);
}
