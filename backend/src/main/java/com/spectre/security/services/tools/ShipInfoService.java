package com.spectre.security.services.tools;

import com.spectre.model.Ship;
import com.spectre.payload.tools.ShipDetailsDto;
import com.spectre.repository.ShipRepository;

import java.util.List;
import java.util.Objects;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ShipInfoService {

    @Autowired
    private ShipRepository shipRepository;

    public ShipDetailsDto getShipDetailsByName(String name) {
        Ship ship = shipRepository.findByName(name).orElseThrow(() -> new RuntimeException("Ship not found"));

        return ShipDetailsDto.builder()
            .name(ship.getName())
            .manufacturer(ship.getManufacturer())
            .type(ship.getType())
            .focus(ship.getFocus())
            .length(ship.getLength())
            .beam(ship.getBeam())
            .height(ship.getHeight())
            .sizeClass(ship.getSizeClass())
            .size(ship.getSize())
            .crewMin(ship.getCrewMin())
            .mass(ship.getMass())
            .cargo(ship.getCargoCapacity())
            .hp(ship.getHp())
            .scmSpeed(ship.getScmSpeed())
            .navMaxSpeed(ship.getNavMaxSpeed())
            .pitch(ship.getPitch())
            .yaw(ship.getYaw())
            .roll(ship.getRoll())
            .insuranceClaimTime(ship.getInsuranceClaimTime())
            .productionStatus(ship.getProductionStatus())
            .productionNote(ship.getProductionNote())
            .msrp(ship.getMsrp())
            .pledgeUrl(ship.getPledgeUrl())
            .description(ship.getDescription())
            .image(ship.getShipImage())
            .build();
        }

    private Double tryParseDouble(String str) {
        try {
            return Double.parseDouble(str);
        } catch (Exception e) {
            return null;
        }
    }
    public List<String> getAllShipNames() {
        return shipRepository.findAll()
            .stream()
            .map(Ship::getName)
            .filter(Objects::nonNull)
            .sorted()
            .toList();
    }
    
}
