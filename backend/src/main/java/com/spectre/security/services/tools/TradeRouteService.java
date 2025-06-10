package com.spectre.security.services.tools;

import com.spectre.cache.CommodityCache;
import com.spectre.payload.tools.TradeRouteRequestDto;
import com.spectre.payload.tools.TradeRouteResponseDto;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

@Service
public class TradeRouteService {
    

    private final CommodityCache commodityCache;
    private final RestTemplate restTemplate = new RestTemplate();

    @Value("${uex.api.token}")
    private String apiToken;

    private static final String BASE_URL = "https://api.uexcorp.space/2.0";

    private static final Map<String, String> riskLevels = Map.of(
            "Stanton", "Low",
            "Pyro", "High"
    );

    public TradeRouteService(CommodityCache commodityCache) {
        this.commodityCache = commodityCache;
    }

    public TradeRouteResponseDto calculateTradeRoute(TradeRouteRequestDto request) {
        String commodityName = normalize(request.getCommodityName());
        int quantity = Math.max(request.getQuantity(), 1);
        String currentSystem = request.getCurrentSystem() != null ? request.getCurrentSystem().trim() : "Stanton";
        boolean allowSystemChange = request.isCanTravelSystems();

        Integer commodityId = commodityCache.getIdByName(commodityName);
        if (commodityId == null) {
            return createError("Commodity not found in cache.", currentSystem);
        }

        try {
            String url = BASE_URL + "/commodities_prices?id_commodity=" + commodityId;
            ResponseEntity<String> response = restTemplate.exchange(
                    url,
                    HttpMethod.GET,
                    new HttpEntity<>(createHeaders()),
                    String.class
            );

            JSONObject body = new JSONObject(response.getBody());
            JSONArray data = body.getJSONArray("data");

            double bestBuy = Double.MAX_VALUE;
            String bestBuyLoc = null;

            double bestSell = -1;
            String bestSellLoc = null;
            String system = null;

            for (int i = 0; i < data.length(); i++) {
                JSONObject entry = data.getJSONObject(i);
                double sellPrice = entry.optDouble("price_sell", 0);
                double buyPrice = entry.optDouble("price_buy", 0);
                String terminal = entry.optString("terminal_name", "Unknown");
                String systemName = entry.optString("star_system_name", "Unknown");

                boolean sameSystem = systemName.equalsIgnoreCase(currentSystem);
                if (!allowSystemChange && !sameSystem) continue;

                if (sellPrice > bestSell) {
                    bestSell = sellPrice;
                    bestSellLoc = terminal;
                    system = systemName;
                }

                if (buyPrice > 0 && buyPrice < bestBuy) {
                    bestBuy = buyPrice;
                    bestBuyLoc = terminal;
                }
            }

            if (bestSellLoc == null || bestBuyLoc == null || bestBuy == Double.MAX_VALUE) {
                return createError("No valid trade route found.", currentSystem);
            }

            double profit = (bestSell - bestBuy) * quantity;
            String risk = riskLevels.getOrDefault(system, "Unknown");

            return TradeRouteResponseDto.builder()
                    .bestBuyLocation(bestBuyLoc)
                    .bestBuyPrice(bestBuy)
                    .bestSellLocation(bestSellLoc)
                    .sellPrice(bestSell)
                    .system(system)
                    .riskLevel(risk)
                    .expectedProfit(profit)
                    .recommendation(
                            "🛒 Buy at " + bestBuyLoc + " for " + bestBuy + " aUEC\n" +
                            "📦 Sell at " + bestSellLoc + " in " + system + " for " + bestSell + " aUEC\n" +
                            "💰 Profit: " + profit + " aUEC"
                    )
                    .build();

        } catch (Exception e) {
            return createError("Failed to retrieve trade route data.", currentSystem);
        }
    }

    private HttpHeaders createHeaders() {
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(apiToken);
        headers.setContentType(MediaType.APPLICATION_JSON);
        return headers;
    }

    private String normalize(String input) {
        return input != null ? input.toLowerCase().replaceAll("[^a-z0-9]", "") : "";
    }

    private TradeRouteResponseDto createError(String message, String system) {
        return TradeRouteResponseDto.builder()
                .bestBuyLocation("N/A")
                .bestBuyPrice(0)
                .bestSellLocation("N/A")
                .sellPrice(0)
                .system(system)
                .riskLevel("Unknown")
                .expectedProfit(0)
                .recommendation(message)
                .build();
    }
}
