package com.spectre.security.services;

import com.spectre.model.ApiShip;
import com.spectre.model.Ship;
import com.spectre.repository.ApiShipRepository;
import com.spectre.repository.ShipRepository;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class ApiShipToShipSyncService {

    private final ApiShipRepository apiShipRepository;
    private final ShipRepository shipRepository;
    @Transactional


    public void syncApiShipsToMainShips() {
        List<ApiShip> apiShips = apiShipRepository.findAll();

        for (ApiShip api : apiShips) {
            log.info("📡 Starting API fetch inside ShipScraperService...");
            if (api.getUuid() == null || api.getUuid().isEmpty()) {
                continue; 
            }

            Optional<Ship> existing = shipRepository.findByUuid(api.getUuid());
            if (existing.isPresent()) {
                Ship ship = existing.get();
                updateShipFromApi(ship, api);

                ship.setName(Optional.ofNullable(api.getName()).orElse(ship.getName()));
                ship.setManufacturer(Optional.ofNullable(api.getManufacturer()).orElse(ship.getManufacturer()));
                ship.setType(Optional.ofNullable(api.getType()).orElse(ship.getType()));
                ship.setFocus(Optional.ofNullable(api.getFocus()).orElse(ship.getFocus()));
                ship.setSize(Optional.ofNullable(api.getSize()).orElse(ship.getSize()));
                ship.setProductionStatus(Optional.ofNullable(api.getProductionStatus()).orElse(ship.getProductionStatus()));
                ship.setProductionNote(Optional.ofNullable(api.getProductionNote()).orElse(ship.getProductionNote()));
                ship.setDescription(Optional.ofNullable(api.getDescription()).orElse(ship.getDescription()));
                ship.setPledgeUrl(Optional.ofNullable(api.getPledgeUrl()).orElse(ship.getPledgeUrl()));
                
                if (api.getCrewMin() != null)              ship.setCrewMin(api.getCrewMin());
                if (api.getSizeClass() != null)            ship.setSizeClass(api.getSizeClass());
                if (api.getShieldHp() != null)             ship.setShieldHp(api.getShieldHp());
                if (api.getLength() != null)               ship.setLength(api.getLength().floatValue());
                if (api.getBeam() != null)                 ship.setBeam(api.getBeam().floatValue());
                if (api.getHeight() != null)               ship.setHeight(api.getHeight().floatValue());
                if (api.getMass() != null)                 ship.setMass(api.getMass().floatValue());
                if (api.getCargoCapacity() != null)        ship.setCargoCapacity(api.getCargoCapacity().floatValue());
                if (api.getPitch() != null)                ship.setPitch(api.getPitch().floatValue());
                if (api.getYaw() != null)                  ship.setYaw(api.getYaw().floatValue());
                if (api.getRoll() != null)                 ship.setRoll(api.getRoll().floatValue());
                if (api.getInsuranceClaimTime() != null)   ship.setInsuranceClaimTime(api.getInsuranceClaimTime().floatValue());
                if (api.getMsrp() != null)                 ship.setMsrp(api.getMsrp());
                if (api.getScmSpeed() != null)             ship.setScmSpeed(api.getScmSpeed().intValue());
                if (api.getMaxSpeed() != null)             ship.setNavMaxSpeed(api.getMaxSpeed().intValue());
                
                if (api.getUpdatedAt() != null) {
                    try {
                        LocalDate parsedDate = LocalDate.parse(api.getUpdatedAt()); 
                        ship.setUpdatedAt(parsedDate);
                    } catch (DateTimeParseException e) {
                    }
                }
                shipRepository.save(ship);
            }
        }
    }

    private void updateShipFromApi(Ship ship, ApiShip api) {
        ship.setName(Optional.ofNullable(api.getName()).orElse(ship.getName()));
        ship.setManufacturer(Optional.ofNullable(api.getManufacturer()).orElse(ship.getManufacturer()));
        ship.setType(Optional.ofNullable(api.getType()).orElse(ship.getType()));
        ship.setFocus(Optional.ofNullable(api.getFocus()).orElse(ship.getFocus()));
        ship.setSize(Optional.ofNullable(api.getSize()).orElse(ship.getSize()));
        ship.setProductionStatus(Optional.ofNullable(api.getProductionStatus()).orElse(ship.getProductionStatus()));
        ship.setProductionNote(Optional.ofNullable(api.getProductionNote()).orElse(ship.getProductionNote()));
        ship.setDescription(Optional.ofNullable(api.getDescription()).orElse(ship.getDescription()));
        ship.setPledgeUrl(Optional.ofNullable(api.getPledgeUrl()).orElse(ship.getPledgeUrl()));
    
        if (api.getCrewMin() != null)              ship.setCrewMin(api.getCrewMin());
        if (api.getSizeClass() != null)            ship.setSizeClass(api.getSizeClass());
        if (api.getShieldHp() != null)             ship.setShieldHp(api.getShieldHp());
    
        if (api.getLength() != null)               ship.setLength(api.getLength().floatValue());
        if (api.getBeam() != null)                 ship.setBeam(api.getBeam().floatValue());
        if (api.getHeight() != null)               ship.setHeight(api.getHeight().floatValue());
        if (api.getMass() != null)                 ship.setMass(api.getMass().floatValue());
        if (api.getCargoCapacity() != null)        ship.setCargoCapacity(api.getCargoCapacity().floatValue());
        if (api.getPitch() != null)                ship.setPitch(api.getPitch().floatValue());
        if (api.getYaw() != null)                  ship.setYaw(api.getYaw().floatValue());
        if (api.getRoll() != null)                 ship.setRoll(api.getRoll().floatValue());
        if (api.getInsuranceClaimTime() != null)   ship.setInsuranceClaimTime(api.getInsuranceClaimTime().floatValue());
        if (api.getMsrp() != null)                 ship.setMsrp(api.getMsrp());
        if (api.getScmSpeed() != null)             ship.setScmSpeed(api.getScmSpeed().intValue());
        if (api.getMaxSpeed() != null)             ship.setNavMaxSpeed(api.getMaxSpeed().intValue());
    }
    
}
