package com.spectre.cache;

import com.spectre.model.PriceEntry;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;

@Component
@RequiredArgsConstructor
public class CommodityPriceCacheRefresher {

    private final CommodityPriceCache priceCache;

    @Value("${uex.api.token}")
    private String apiToken;

    private static final String API_URL = "https://api.uexcorp.space/2.0/commodities_prices_all";

    @PostConstruct
    @Scheduled(fixedRate = 3600000) 
    public void refreshPrices() {
        try {
            RestTemplate restTemplate = new RestTemplate();
            HttpHeaders headers = new HttpHeaders();
            headers.set("Authorization", "Bearer " + apiToken);

            ResponseEntity<String> response = restTemplate.exchange(
                    API_URL,
                    HttpMethod.GET,
                    new HttpEntity<>(headers),
                    String.class
            );

            if (response.getStatusCode() != HttpStatus.OK) {
                System.err.println("❌ Failed to fetch commodity prices: " + response.getStatusCode());
                return;
            }

            JSONObject body = new JSONObject(response.getBody());
            JSONArray data = body.getJSONArray("data");

            List<PriceEntry> entries = new ArrayList<>();
            for (int i = 0; i < data.length(); i++) {
                JSONObject json = data.getJSONObject(i);

                PriceEntry entry = PriceEntry.builder()
                .commodityId(json.optString("id_commodity"))
                .commodityName(json.optString("commodity_name"))
                .terminal(json.optString("terminal_name"))
                .terminalName(json.optString("terminal_name"))
                .priceBuy(json.optDouble("price_buy", 0))
                .priceSell(json.optDouble("price_sell", 0))
                .isBuyable(json.optInt("status_buy", 0) == 1)
                .isSellable(json.optInt("status_sell", 0) == 1)
                .build();
                
                entries.add(entry);
            }

            priceCache.updateCache(entries);
            System.out.println("✅ Loaded " + entries.size() + " price entries into cache");

        } catch (Exception e) {
            System.err.println("❌ Error during price cache refresh: " + e.getMessage());
        }
    }
}
