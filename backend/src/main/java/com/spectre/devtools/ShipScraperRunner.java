package com.spectre.devtools;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.spectre.cache.ShipCache;
import com.spectre.cache.ShipCacheLoader;
import com.spectre.model.ApiShip;
import com.spectre.payload.tools.ShipScraperService;
import com.spectre.security.services.ApiShipToShipSyncService;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.*;

@Slf4j
@Component
@RequiredArgsConstructor
public class ShipScraperRunner implements CommandLineRunner {

    @Autowired
    private final ShipScraperService shipScraperService;

    @Autowired
    private final ApiShipToShipSyncService apiShipToShipSyncService;

    @Autowired
    private ShipCacheLoader shipCacheLoader;


    private static final String API_URL = "https://api.star-citizen.wiki/api/v3/vehicles?page=%d";
    private final HttpClient httpClient = HttpClient.newHttpClient();
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public void run(String... args) {
        log.info("🧠 Initializing ship cache...");

        try {

            //shipScraperService.scrapeFromExistingUuids();

            log.info("🔁 Syncing ships from api_ships to ships table...");
            //apiShipToShipSyncService.syncApiShipsToMainShips();

            log.info("📡 Fetching all API ships to track missing ones...");
            List<ApiShip> allShips = loadAllShipsFromApi();
            log.info("🧠 Retrieved {} ships from the API.", allShips.size());

            List<ApiShip> filtered = allShips.stream()
                    .filter(ship -> trackedShipNames().contains(ship.getName()))
                    .toList();


            log.info("✅ Ship scraping and syncing complete.");
        } catch (Exception e) {
            log.error("❌ Error during startup ship sync", e);
        }
    }

    private List<ApiShip> loadAllShipsFromApi() {
        List<ApiShip> result = new ArrayList<>();
        int page = 1;

        while (true) {
            try {
                HttpRequest request = HttpRequest.newBuilder()
                        .uri(URI.create(String.format(API_URL, page)))
                        .header("Accept", "application/json")
                        .GET()
                        .build();

                HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

                if (response.statusCode() != 200) {
                    log.warn("⚠️ API request failed on page {}: {}", page, response.body());
                    break;
                }

                JsonNode root = objectMapper.readTree(response.body());
                JsonNode data = root.path("data");

                if (!data.isArray() || data.isEmpty()) break;

                List<ApiShip> shipsOnPage = objectMapper.convertValue(data, new TypeReference<>() {});
                result.addAll(shipsOnPage);
                page++;

            } catch (Exception e) {
                log.error("❌ Error fetching page {}: {}", page, e.getMessage(), e);
                break;
            }
        }

        return result;
    }

    private Set<String> trackedShipNames() {
        return Set.of(
            "Apollo Medivac", "Apollo Triage", "Arrastra", "ATLS", "ATLS GEO",
            "Crucible", "CSV-SM", "E1 Spirit", "Endeavor",
            "Expanse", "G12", "G12a", "G12r", "Galaxy", "Genesis",
            "Hull B", "Hull D", "Hull E", "Idris-P", "Ironclad", "Ironclad Assault",
            "Kraken", "Kraken Privateer", "Legionnaire", "Liberator", "Merchantman", "MTC",
            "Nautilus", "Odyssey", "Orion", "Paladin", "Perseus", "Pioneer",
            "Pirate Gladius", "Railen", "Ranger CV", "Ranger RC", "Ranger TR",
            "Reclaimer Best In Show Edition 2949", "Retaliator", "Starlancer TAC",
            "Vulcan", "Zeus Mk II MR"
        );
    }
    
}
