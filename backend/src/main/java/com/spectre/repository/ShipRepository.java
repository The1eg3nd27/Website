package com.spectre.repository;

import com.spectre.model.Ship;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ShipRepository extends JpaRepository<Ship, Long> {

    Optional<Ship> findByName(String name);

    Optional<Ship> findByNameIgnoreCase(String name);

    List<Ship> findByManufacturerIgnoreCase(String manufacturer);

    List<Ship> findByTypeIgnoreCase(String type);

    List<Ship> findByFocusIgnoreCase(String focus);

}
