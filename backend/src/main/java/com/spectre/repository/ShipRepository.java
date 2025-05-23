package com.spectre.repository;

import com.spectre.model.Ship;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface ShipRepository extends JpaRepository<Ship, Long> {
   Optional<Ship> findByName(String name);
   Optional<Ship> findByUuid(String uuid);
   @Query("SELECT s.uuid FROM Ship s WHERE s.uuid IS NOT NULL")
   List<String> findAllUuids();
}
