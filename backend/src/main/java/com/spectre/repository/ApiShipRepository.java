package com.spectre.repository;

import com.spectre.model.ApiShip;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ApiShipRepository extends JpaRepository<ApiShip, Long> {
    Optional<ApiShip> findByUuid(String uuid);
    boolean existsByUuid(String uuid);
}
