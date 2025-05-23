package com.spectre.security.services.tools;

import com.spectre.model.Ship;
import com.spectre.payload.tools.ShipComparisonResultDto;
import com.spectre.repository.ShipRepository;
import lombok.RequiredArgsConstructor;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ShipComparisonService {

    private final ShipRepository shipRepository;

    public ShipComparisonResultDto compare(String name1, String name2) {
        if (name1 == null || name2 == null || name1.trim().isEmpty() || name2.trim().isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Ship names must not be null or empty");
        }

        Ship ship1 = findClosestMatch(name1);
        Ship ship2 = findClosestMatch(name2);

        return ShipComparisonResultDto.builder()
                .ship1(buildDto(ship1))
                .ship2(buildDto(ship2))
                .build();
    }


    private Ship findClosestMatch(String inputName) {
        if (inputName == null || inputName.trim().isEmpty()) {
            throw new IllegalArgumentException("Ship name must not be null or empty");
        }
    
        String searchName = inputName.toLowerCase();
    
        return shipRepository.findAll().stream()
                .filter(ship -> ship.getName() != null) 
                .min((s1, s2) -> Integer.compare(
                        levenshteinDistance(s1.getName().toLowerCase(), searchName),
                        levenshteinDistance(s2.getName().toLowerCase(), searchName)
                ))
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "No ship match found for: " + inputName));
    }
    

    private ShipComparisonResultDto.ShipStats buildDto(Ship ship) {
        return ShipComparisonResultDto.ShipStats.builder()
                .name(ship.getName())
                .manufacturer(ship.getManufacturer())
                .type(ship.getType())
                .focus(nullSafe(ship.getFocus()))
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
                .maxSpeed(ship.getNavMaxSpeed())
                .pitch(ship.getPitch())
                .yaw(ship.getYaw())
                .roll(ship.getRoll())
                .insuranceClaimTime(ship.getInsuranceClaimTime())
                .productionStatus(nullSafe(ship.getProductionStatus()))
                .productionNote(nullSafe(ship.getProductionNote()))
                .msrp(ship.getMsrp())
                .pledgeUrl(nullSafe(ship.getPledgeUrl()))
                .description(nullSafe(ship.getDescription()))
                .image(ship.getShipImage())
                .build();
        }

    private int levenshteinDistance(String a, String b) {
        int[][] dp = new int[a.length() + 1][b.length() + 1];

        for (int i = 0; i <= a.length(); i++) dp[i][0] = i;
        for (int j = 0; j <= b.length(); j++) dp[0][j] = j;

        for (int i = 1; i <= a.length(); i++) {
            for (int j = 1; j <= b.length(); j++) {
                int cost = a.charAt(i - 1) == b.charAt(j - 1) ? 0 : 1;
                dp[i][j] = Math.min(
                        Math.min(dp[i - 1][j] + 1, dp[i][j - 1] + 1),
                        dp[i - 1][j - 1] + cost
                );
            }
        }

        return dp[a.length()][b.length()];
    }

    private String nullSafe(String value) {
        return value != null ? value : "Unknown";
    }
}
