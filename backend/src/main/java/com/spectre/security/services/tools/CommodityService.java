package com.spectre.security.services.tools;

import com.spectre.cache.CommodityCache;
import com.spectre.cache.CommodityPriceCache;
import com.spectre.model.PriceEntry;
import com.spectre.payload.tools.CommodityRequestDto;
import com.spectre.payload.tools.CommodityResponseDto;

import org.apache.commons.text.similarity.LevenshteinDistance;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.*;

@Service
@RequiredArgsConstructor
public class CommodityService {

    private final CommodityCache commodityCache;
    private final CommodityPriceCache commodityPriceCache;

    @Value("${uex.api.token}")
    private String apiToken;

    private static final String BASE_URL = "https://api.uexcorp.space/2.0";

    private final RestTemplate restTemplate = new RestTemplate();

    @PostConstruct
    @Scheduled(fixedRate = 3600000)
    public void loadCommodities() {
        try {
            HttpHeaders headers = createHeaders();
            HttpEntity<Void> entity = new HttpEntity<>(headers);

            ResponseEntity<String> response = restTemplate.exchange(
                    BASE_URL + "/commodities",
                    HttpMethod.GET,
                    entity,
                    String.class
            );

            if (!response.getStatusCode().is2xxSuccessful()) {
                throw new IllegalStateException("Failed to fetch commodities: " + response.getStatusCode());
            }

            JSONObject json = new JSONObject(response.getBody());
            JSONArray data = json.getJSONArray("data");

            Map<String, CommodityResponseDto> tempMap = new HashMap<>();

            for (int i = 0; i < data.length(); i++) {
                JSONObject item = data.getJSONObject(i);
                String id = item.opt("id").toString();
                String name = item.optString("name");

                if (id != null && name != null && !name.isBlank()) {
                    tempMap.put(name.toLowerCase(), CommodityResponseDto.builder()
                            .id(id)
                            .commodityName(name)
                            .build());
                }
            }

            commodityCache.updateCache(tempMap);
            System.out.println("✅ Loaded " + tempMap.size() + " commodities into cache");

        } catch (Exception e) {
            System.err.println("❌ Failed to load commodities: " + e.getMessage());
        }
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
        Optional<CommodityResponseDto> base = resolveByName(request.getCommodity());

        if (base.isEmpty()) {
            return CommodityResponseDto.builder()
                    .commodityName(request.getCommodity())
                    .fallbackInfo("Commodity not found.")
                    .build();
        }

        String id = base.get().getId();
        String name = base.get().getCommodityName();
        int quantity = Math.max(request.getQuantity(), 1);

        List<PriceEntry> prices = commodityPriceCache.getPricesForCommodity(id);
        if (prices == null || prices.isEmpty()) {
            return CommodityResponseDto.builder()
                    .commodityName(name)
                    .fallbackInfo("No price data found.")
                    .build();
        }
        System.out.println("🔍 Lookup for commodity: " + name + " (ID: " + id + ")");
        System.out.println("📦 Prices found: " + prices.size());

        double bestBuy = Double.MAX_VALUE;
        double bestSell = 0;
        String bestBuyLoc = null;
        String bestSellLoc = null;

        for (PriceEntry entry : prices) {
            if (entry.getPriceBuy() > 0 && entry.getPriceBuy() < bestBuy) {
                bestBuy = entry.getPriceBuy();
                bestBuyLoc = entry.getTerminal() != null ? entry.getTerminal() : entry.getTerminalName();
            }
            if (entry.getPriceSell() > bestSell) {
                bestSell = entry.getPriceSell();
                bestSellLoc = entry.getTerminal();
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

        
    }

    public Optional<CommodityResponseDto> resolveByName(String input) {
        if (input == null || input.isBlank()) return Optional.empty();

        CommodityResponseDto exact = commodityCache.getByName(input);
        if (exact != null) return Optional.of(exact);
        LevenshteinDistance distanceCalculator = new LevenshteinDistance();
        return commodityCache.getAll().stream()
            .filter(c -> c.getCommodityName() != null)
            .min(Comparator.comparingInt(c ->
                distanceCalculator.apply(c.getCommodityName().toLowerCase(), input.toLowerCase())))
            .filter(c -> distanceCalculator.apply(c.getCommodityName().toLowerCase(), input.toLowerCase()) < 3);
    }
    
    private HttpHeaders createHeaders() {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + apiToken);
        return headers;
    }

    public CommodityResponseDto getBestSellInfo(String name, int quantity) {
        System.out.println("🔍 getBestSellInfo called with: " + name);
    
        CommodityResponseDto base = commodityCache.getByName(name);
        if (base == null) {
            System.out.println("❌ No commodity found for name: " + name);
            return CommodityResponseDto.builder()
                .fallbackInfo("Commodity not found.")
                .build();
        }
    
        String id = base.getId();
        System.out.println("✅ Found ID: " + id + " for name: " + name);
    
        List<PriceEntry> prices = commodityPriceCache.getPricesForCommodity(id);
        if (prices == null || prices.isEmpty()) {
            System.out.println("❌ No price data for ID: " + id);
            return CommodityResponseDto.builder()
                .commodityName(name)
                .fallbackInfo("No price data found.")
                .build();
        }
    
        System.out.println("📦 Prices found: " + prices.size());
        for (PriceEntry p : prices) {
            System.out.println("📍 " + p.getTerminal() + " | Buy: " + p.getPriceBuy() + " | Sell: " + p.getPriceSell());
        }
    
        PriceEntry bestBuy = prices.stream()
            .filter(p -> p.getPriceBuy() > 0)
            .min(Comparator.comparingDouble(PriceEntry::getPriceBuy))
            .orElse(null);
    
        PriceEntry bestSell = prices.stream()
            .filter(p -> p.getPriceSell() > 0)
            .max(Comparator.comparingDouble(PriceEntry::getPriceSell))
            .orElse(null);
    
        if (bestBuy == null && bestSell == null) {
            System.out.println("⚠️ No buy or sell prices available");
            return CommodityResponseDto.builder()
                .commodityName(name)
                .fallbackInfo("No buy or sell prices available.")
                .build();
        }
    
        if (bestBuy == null) {
            System.out.println("⚠️ No buy location found!");
            return CommodityResponseDto.builder()
                .id(id)
                .commodityName(name)
                .bestSellLocation(bestSell.getTerminal())
                .bestSellPrice(bestSell.getPriceSell())
                .profitPerUnit(0.0)
                .potentialProfit(0.0)
                .recommendation("Only sell price available at " + bestSell.getTerminal())
                .build();
        }
    
        if (bestSell == null) {
            System.out.println("⚠️ No sell location found!");
            return CommodityResponseDto.builder()
                .id(id)
                .commodityName(name)
                .bestBuyLocation(bestBuy.getTerminal())
                .bestBuyPrice(bestBuy.getPriceBuy())
                .profitPerUnit(0.0)
                .potentialProfit(0.0)
                .recommendation("Only buy price available at " + bestBuy.getTerminal())
                .build();
        }
    
        double profitPerUnit = bestSell.getPriceSell() - bestBuy.getPriceBuy();
        double totalProfit = profitPerUnit * quantity;
    
        System.out.println("✅ Best Buy: " + bestBuy.getTerminal() + " @ " + bestBuy.getPriceBuy());
        System.out.println("✅ Best Sell: " + bestSell.getTerminal() + " @ " + bestSell.getPriceSell());
    
        return CommodityResponseDto.builder()
            .id(id)
            .commodityName(name)
            .bestBuyLocation(bestBuy.getTerminal())
            .bestBuyPrice(bestBuy.getPriceBuy())
            .bestSellLocation(bestSell.getTerminal())
            .bestSellPrice(bestSell.getPriceSell())
            .profitPerUnit(profitPerUnit)
            .potentialProfit(totalProfit)
            .recommendation("Buy at " + bestBuy.getTerminal() + " → Sell at " + bestSell.getTerminal())
            .build();
    }
    
}