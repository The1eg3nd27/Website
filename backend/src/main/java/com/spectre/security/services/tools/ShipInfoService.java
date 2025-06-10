package com.spectre.security.services.tools;

import com.spectre.cache.ShipCache;
import com.spectre.model.Ship;
import com.spectre.payload.tools.ShipDetailsDto;
import com.spectre.repository.ShipRepository;
import lombok.RequiredArgsConstructor;
import org.apache.commons.text.similarity.LevenshteinDistance;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class ShipInfoService {

    private final ShipRepository shipRepository;
    private final ShipCache shipCache;

    public ShipDetailsDto getDetailsByName(String queryName) {
        Optional<Ship> direct = shipRepository.findByNameIgnoreCase(queryName);
        if (direct.isPresent()) {
            return mapToDto(direct.get());
        }

        Ship closest = shipCache.getShips().stream()
                .min(Comparator.comparingInt(s -> LevenshteinDistance.getDefaultInstance()
                        .apply(queryName.toLowerCase(), s.getName().toLowerCase())))
                .orElseThrow(() -> new RuntimeException("No ships found"));

        return mapToDto(closest);
    }

    private ShipDetailsDto mapToDto(Ship ship) {
        return ShipDetailsDto.builder()
                .uuid(ship.getUuid())
                .name(ship.getName())
                .manufacturer(ship.getManufacturer())
                .type(ship.getType())
                .focus(ship.getFocus())
                .size(ship.getSize())
                .crewMin(ship.getCrewMin())
                .crewMax(ship.getCrewMax())
                .length(ship.getLength())
                .beam(ship.getBeam())
                .height(ship.getHeight())
                .mass(ship.getMass())
                .cargoCapacity(ship.getCargoCapacity())
                .hp(ship.getHp())
                .shieldHp(ship.getShieldHp())
                .scmSpeed(ship.getScmSpeed())
                .navMaxSpeed(ship.getNavMaxSpeed())
                .pitch(ship.getPitch())
                .yaw(ship.getYaw())
                .roll(ship.getRoll())
                .insuranceClaimTime(ship.getInsuranceClaimTime())
                .productionStatus(ship.getProductionStatus())
                .productionNote(ship.getProductionNote())
                .msrp( ship.getMsrp()) 
                .description(ship.getDescription())
                .pledgeUrl(ship.getPledgeUrl())
                .sizeClass(ship.getSizeClass())
                .build();
    }
}
