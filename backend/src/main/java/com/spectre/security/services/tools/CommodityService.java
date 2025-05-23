package com.spectre.security.services.tools;

import com.spectre.cache.CommodityCache;
import com.spectre.payload.tools.CommodityRequestDto;
import com.spectre.payload.tools.CommodityResponseDto;
import jakarta.annotation.PostConstruct;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.*;

@Service
public class CommodityService {

    private final CommodityCache commodityCache;

    @Value("${uex.api.token}")
    private String apiToken;

    private static final String BASE_URL = "https://api.uexcorp.space/2.0";

    public CommodityService(CommodityCache commodityCache) {
        this.commodityCache = commodityCache;
    }

    @PostConstruct
    @Scheduled(fixedRate = 3600000) // Jede stunde, notfalls anpassen
    public void loadCommodities() {
        try {
            RestTemplate restTemplate = new RestTemplate();
            HttpHeaders headers = createHeaders();
            HttpEntity<Void> entity = new HttpEntity<>(headers);

            String url = BASE_URL + "/commodities";

            ResponseEntity<String> response = restTemplate.exchange(
                    url,
                    HttpMethod.GET,
                    entity,
                    String.class
            );

            if (response.getStatusCode() != HttpStatus.OK) {
                throw new IllegalStateException("Failed to fetch commodities: " + response.getStatusCode());
            }

            JSONObject json = new JSONObject(response.getBody());
            JSONArray data = json.getJSONArray("data");

            Map<String, CommodityResponseDto> tempMap = new HashMap<>();

            for (int i = 0; i < data.length(); i++) {
                JSONObject item = data.getJSONObject(i);
                String id = item.optString("id");
                String name = item.optString("name");

                if (id != null && name != null && !name.isBlank()) {
                    CommodityResponseDto dto = CommodityResponseDto.builder()
                            .id(id)
                            .commodityName(name)
                            .build();
                    tempMap.put(name.toLowerCase(), dto);
                }
            }

            commodityCache.updateCache(tempMap);
            System.out.println("Loaded " + tempMap.size() + " commodities into cache.");

        } catch (Exception e) {
            System.err.println("Failed to load commodities: " + e.getMessage());
        }
    }

    public Optional<CommodityResponseDto> resolveByName(String input) {
        if (input == null || input.isBlank()) return Optional.empty();
        return Optional.ofNullable(commodityCache.getByName(input));
    }

    public Collection<CommodityResponseDto> getAllCommodities() {
        return commodityCache.getAll();
    }

    public List<String> getAllCommodityNames() {
        return commodityCache.getAll().stream()
                .map(CommodityResponseDto::getCommodityName)
                .filter(Objects::nonNull)
                .distinct()
                .sorted()
                .toList();
    }

    public CommodityResponseDto getCommodityInfo(CommodityRequestDto request) {
        Optional<CommodityResponseDto> base = resolveByName(request.getCommodityName());

        if (base.isEmpty()) {
            return CommodityResponseDto.builder()
                    .commodityName(request.getCommodityName())
                    .fallbackInfo("Commodity not found: " + request.getCommodityName())
                    .build();
        }

        String id = base.get().getId();
        String name = base.get().getCommodityName();
        int quantity = Math.max(request.getQuantity(), 1);

        try {
            String url = BASE_URL + "/commodities_prices?id_commodity=" + id;
            RestTemplate restTemplate = new RestTemplate();
            ResponseEntity<String> response = restTemplate.exchange(
                    url,
                    HttpMethod.GET,
                    new HttpEntity<>(createHeaders()),
                    String.class
            );

            JSONObject body = new JSONObject(response.getBody());
            JSONArray data = body.getJSONArray("data");

            String bestBuyLoc = null, bestSellLoc = null;
            double bestBuy = Double.MAX_VALUE, bestSell = 0;

            for (int i = 0; i < data.length(); i++) {
                JSONObject row = data.getJSONObject(i);
                double buy = row.optDouble("price_buy", -1);
                double sell = row.optDouble("price_sell", -1);

                String location = row.optString("terminal_name", row.optString("location", "Unknown"));

                if (buy > 0 && buy < bestBuy) {
                    bestBuy = buy;
                    bestBuyLoc = location;
                }

                if (sell > bestSell) {
                    bestSell = sell;
                    bestSellLoc = location;
                }
            }

            double profitPerUnit = bestSell - bestBuy;
            double totalProfit = profitPerUnit * quantity;

            return CommodityResponseDto.builder()
                    .id(id)
                    .commodityName(name)
                    .bestBuyLocation(bestBuyLoc)
                    .bestBuyPrice(bestBuy)
                    .bestSellLocation(bestSellLoc)
                    .bestSellPrice(bestSell)
                    .profitPerUnit(profitPerUnit)
                    .potentialProfit(totalProfit)
                    .recommendation("Buy at " + bestBuyLoc + " → Sell at " + bestSellLoc)
                    .build();

        } catch (Exception e) {
            return CommodityResponseDto.builder()
                    .commodityName(name)
                    .fallbackInfo("API Error: " + e.getMessage())
                    .build();
        }
    }

    private HttpHeaders createHeaders() {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + apiToken);
        return headers;
    }
}
