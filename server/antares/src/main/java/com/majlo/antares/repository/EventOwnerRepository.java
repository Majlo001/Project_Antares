package com.majlo.antares.repository;

import com.majlo.antares.model.EventOwner;
import com.majlo.antares.model.User;
import jakarta.validation.constraints.NotNull;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EventOwnerRepository extends JpaRepository<EventOwner, Long> {
    boolean existsByEventOwner(@NotNull User eventOwner);
}
