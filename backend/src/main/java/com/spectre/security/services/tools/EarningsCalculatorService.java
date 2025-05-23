package com.spectre.security.services.tools;

import com.spectre.payload.tools.EarningsRequestDTO;
import com.spectre.payload.tools.EarningsResponseDTO;
import org.springframework.stereotype.Service;

@Service
public class EarningsCalculatorService {

    public EarningsResponseDTO calculateEarnings(EarningsRequestDTO dto) {
        double balanceBefore = dto.getBalanceBefore();
        double balanceAfter = dto.getBalanceAfter();
        int numberOfPeople = dto.getPlayerCount();
        String notes = dto.getNote();

        double totalDifference = balanceAfter - balanceBefore;
        double perPerson = numberOfPeople > 0 ? totalDifference / numberOfPeople : 0;
        boolean isProfit = totalDifference >= 0;

        String summary;
        if (isProfit) {
            summary = String.format("🧑‍🦰 Spieler: %d\n\n💸 Gewinn: %.2f\n\n🧮 Pro Person: %.2f\n\n%s",
                    numberOfPeople, totalDifference, perPerson, notes != null ? notes : "");
        } else {
            summary = String.format("🧑‍🦰 Spieler: %d\n\n‼️ Verlust: %.2f\n\n🧮 Pro Person: %.2f\n\n%s",
                    numberOfPeople, totalDifference, perPerson, notes != null ? notes : "");
        }

        EarningsResponseDTO response = new EarningsResponseDTO();
        response.setTotalDifference(totalDifference);
        response.setPerPerson(perPerson);
        response.setProfit(isProfit);
        response.setSummary(summary);
        return response;
    }
}
