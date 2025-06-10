package com.spectre.security.services.tools;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.spectre.payload.tools.CommodityRequestDto;
import com.spectre.payload.tools.TradeRouteResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
@RequiredArgsConstructor
public class TradeRouteService {

    @Value("${uex.api.token}")
    private String uexToken;

    private final RestTemplate restTemplate = new RestTemplate();

    public TradeRouteResponseDto calculateTradeRoute(CommodityRequestDto request) {
        String buyUrl = "https://uexcorp.space/api/commodities/" + request.getCommodity() + "/buy";
        String sellUrl = "https://uexcorp.space/api/commodities/" + request.getCommodity() + "/sell";

        try {
            String buyRaw = restTemplate.getForObject(buyUrl + "?token=" + uexToken, String.class);
            String sellRaw = restTemplate.getForObject(sellUrl + "?token=" + uexToken, String.class);

            ObjectMapper mapper = new ObjectMapper();
            JsonNode buyData = mapper.readTree(buyRaw);
            JsonNode sellData = mapper.readTree(sellRaw);

            JsonNode bestBuy = findBestLocation(buyData, request.getCurrentSystem(), request.getCurrentLocation(), true);
            JsonNode bestSell = findBestLocation(sellData, request.getCurrentSystem(), request.getCurrentLocation(), false);

            if (bestBuy == null || bestSell == null) {
                return TradeRouteResponseDto.builder()
                        .commodity(request.getCommodity())
                        .message("No viable trade route found.")
                        .build();
            }

            double buyPrice = bestBuy.get("price").asDouble();
            double sellPrice = bestSell.get("price").asDouble();
            int quantity = request.getQuantity();
            double totalProfit = (sellPrice - buyPrice) * quantity;
            boolean crossSystem = !bestBuy.get("system").asText().equals(bestSell.get("system").asText());

            return TradeRouteResponseDto.builder()
                    .fromLocation(bestBuy.get("location").asText())
                    .toLocation(bestSell.get("location").asText())
                    .commodity(request.getCommodity())
                    .buyPrice(buyPrice)
                    .sellPrice(sellPrice)
                    .quantity(quantity)
                    .totalProfit(totalProfit)
                    .crossSystem(crossSystem)
                    .message("Best route found.")
                    .build();

        } catch (Exception e) {
            return TradeRouteResponseDto.builder()
                    .commodity(request.getCommodity())
                    .message("Error fetching trade data: " + e.getMessage())
                    .build();
        }
    }

    private JsonNode findBestLocation(JsonNode data, String system, String location, boolean isBuy) {
        JsonNode best = null;
        double bestPrice = isBuy ? Double.MAX_VALUE : Double.MIN_VALUE;

        for (JsonNode entry : data) {
            String entrySystem = entry.get("system").asText();
            String entryLocation = entry.get("location").asText();
            double price = entry.get("price").asDouble();

            boolean validLocation = entrySystem.equals(system) || entryLocation.equals(location);
            if (validLocation && (
                    (isBuy && price < bestPrice) ||
                    (!isBuy && price > bestPrice))) {
                best = entry;
                bestPrice = price;
            }
        }
        return best;
    }
}
