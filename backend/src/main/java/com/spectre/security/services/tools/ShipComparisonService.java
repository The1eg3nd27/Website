package com.spectre.security.services.tools;

import com.spectre.cache.ShipCache;
import com.spectre.model.Ship;
import com.spectre.payload.tools.ShipCompareRequestDto;
import com.spectre.payload.tools.ShipComparisonResultDto;
import com.spectre.payload.tools.ShipComparisonResultDto.ShipStats;
import com.spectre.payload.tools.ShipDetailsDto;
import com.spectre.repository.ShipRepository;
import org.springframework.beans.factory.annotation.Autowired;
import lombok.RequiredArgsConstructor;
import org.apache.commons.text.similarity.LevenshteinDistance;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class ShipComparisonService {

    private final ShipCache shipCache;


    @Autowired
    private ShipRepository shipRepository;


    public ShipComparisonResultDto compare(ShipCompareRequestDto request) {
        String shipAName = request.getShipA();
        String shipBName = request.getShipB();

        Ship ship1 = findClosestMatch(shipAName);
        Ship ship2 = findClosestMatch(shipBName);

        return ShipComparisonResultDto.builder()
                .shipA(toStats(ship1))
                .shipB(toStats(ship2))
                .build();
    }

    private ShipComparisonResultDto.ShipStats toStats(Ship ship) {
        return ShipComparisonResultDto.ShipStats.builder()
                .name(ship.getName())
                .manufacturer(ship.getManufacturer())
                .type(ship.getType())
                .focus(ship.getFocus())
                .size(ship.getSize())
                .crewMin(ship.getCrewMin())
                .length(ship.getLength())
                .beam(ship.getBeam())
                .height(ship.getHeight())
                .mass(ship.getMass())
                .cargoCapacity(ship.getCargoCapacity())
                .shieldHp(ship.getShieldHp())
                .scmSpeed(ship.getScmSpeed())
                .navMaxSpeed(ship.getNavMaxSpeed())
                .pitch(ship.getPitch())
                .yaw(ship.getYaw())
                .roll(ship.getRoll())
                .insuranceClaimTime(ship.getInsuranceClaimTime())
                .productionStatus(ship.getProductionStatus())
                .productionNote(ship.getProductionNote())
                .msrp((double) ship.getMsrp()) // ✅ cast float → double
                .description(ship.getDescription())
                .pledgeUrl(ship.getPledgeUrl())
                .sizeClass(ship.getSizeClass())
                .build();
    }


    public ShipComparisonResultDto compareShips(String shipA, String shipB) {
        Ship shipEntityA = findClosestMatch(shipA);
        Ship shipEntityB = findClosestMatch(shipB);
    
        if (shipEntityA == null || shipEntityB == null) {
            throw new RuntimeException("❌ One or both ships not found.");
        }
    
        return new ShipComparisonResultDto(
            toStats(shipEntityA),
            toStats(shipEntityB)
        );
    }

    private Ship findClosestMatch(String query) {
        if (query == null || query.isBlank()) {
            throw new IllegalArgumentException("Ship query must not be null or blank");
        }
    
        return shipCache.getShips().stream()
                .filter(ship -> ship.getName() != null) 
                .min(Comparator.comparingInt(ship ->
                        LevenshteinDistance.getDefaultInstance().apply(
                                query.toLowerCase(),
                                ship.getName().toLowerCase()
                        )
                ))
                .orElseThrow(() -> new RuntimeException("No matching ship found for: " + query));
    }
    
    public List<String> getAllShipNames() {
        return shipCache.getShips().stream()
                .map(Ship::getName)
                .filter(Objects::nonNull)
                .sorted()
                .collect(Collectors.toList());
    }

    public ShipDetailsDto getShipDetails(String name) {
        Ship ship = shipRepository.findByNameIgnoreCase(name)
            .orElseThrow(() -> new RuntimeException("Ship not found: " + name));
        
            return ShipDetailsDto.fromEntity(ship); 
    }

    
}
