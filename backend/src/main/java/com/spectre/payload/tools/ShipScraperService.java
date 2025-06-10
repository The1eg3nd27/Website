package com.spectre.payload.tools;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.spectre.model.ApiShip;
import com.spectre.repository.ApiShipRepository;
import com.spectre.repository.ShipRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class ShipScraperService {

    private final ApiShipRepository apiShipRepository;
    private final ShipRepository shipRepository;
    private final HttpClient httpClient = HttpClient.newHttpClient();
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Scheduled(cron = "0 0 3 * * MON")
    public void scheduledShipSync() {
        log.info("📅 Running weekly ShipScraperService scheduled task...");
        scrapeFromExistingUuids();
    }

    public void scrapeFromExistingUuids() {
        List<String> uuids = shipRepository.findAllUuids();
        List<ApiShip> toSave = new ArrayList<>();
        int total = 0;

        for (String uuid : uuids) {
            try {
                URI uri = URI.create("https://api.star-citizen.wiki/api/v3/vehicles/" + uuid);
                HttpRequest request = HttpRequest.newBuilder()
                        .uri(uri)
                        .header("Accept", "application/json")
                        .GET()
                        .build();

                HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

                if (response.statusCode() != 200) {
                    log.warn("❌ Could not fetch data for UUID {}: {}", uuid, response.body());
                    continue;
                }

                JsonNode root = objectMapper.readTree(response.body());
                JsonNode data = root.path("data");
                if (data.isMissingNode() || data.isEmpty()) {
                    log.warn("⏭️ No valid data for UUID {}", uuid);
                    continue;
                }

                ApiShip ship = new ApiShip();

                ship.setUuid(uuid);
                ship.setName(getNullableString(data, "name"));
                ship.setManufacturer(getNullableString(data.path("manufacturer"), "name"));
                ship.setType(getNullableString(data.path("type"), "en_EN"));
                ship.setFocus(getNullableStringFromArray(data.path("foci"), "en_EN"));
                ship.setSize(getNullableString(data.path("size"), "en_EN"));
                ship.setSizeClass(getNullableInt(data, "size_class"));

                ship.setLength(getNullableDouble(data.path("sizes"), "length"));
                ship.setBeam(getNullableDouble(data.path("sizes"), "beam"));
                ship.setHeight(getNullableDouble(data.path("sizes"), "height"));

                ship.setCrewMin(getNullableInt(data.path("crew"), "min"));
                ship.setCrewMax(getNullableInt(data.path("crew"), "max"));

                ship.setMass(getNullableDouble(data, "mass"));
                ship.setCargoCapacity(getNullableDouble(data, "cargo_capacity"));
                ship.setHealth(getNullableInt(data, "health"));
                ship.setShieldHp(getNullableInt(data, "shield_hp"));

                ship.setScmSpeed(getNullableDouble(data.path("speed"), "scm"));
                ship.setMaxSpeed(getNullableDouble(data.path("speed"), "max"));

                ship.setPitch(getNullableDouble(data.path("agility"), "pitch"));
                ship.setYaw(getNullableDouble(data.path("agility"), "yaw"));
                ship.setRoll(getNullableDouble(data.path("agility"), "roll"));

                ship.setInsuranceClaimTime(getNullableDouble(data.path("insurance"), "claim_time"));

                ship.setProductionStatus(getNullableString(data.path("production_status"), "en_EN"));
                ship.setProductionNote(getNullableString(data.path("production_note"), "en_EN"));
                ship.setDescription(getNullableString(data.path("description"), "en_EN"));
                ship.setMsrp(getNullableDouble(data, "msrp"));
                ship.setPledgeUrl(getNullableString(data, "pledge_url"));
                ship.setUpdatedAt(getNullableString(data, "updated_at"));
                ship.setLink(getNullableString(data, "link"));

                // ✅ Upsert
                Optional<ApiShip> existing = apiShipRepository.findByUuid(uuid);
                existing.ifPresent(e -> ship.setId(e.getId()));

                apiShipRepository.save(ship);
                total++;
                log.info("✅ Synced ship: {} ({})", ship.getName(), uuid);

            } catch (Exception e) {
                log.error("❌ Error fetching or parsing ship for UUID {}", uuid, e);
            }
        }

        log.info("✅ Completed sync. Total ships updated or saved: {}", total);
    }


    private String getNullableString(JsonNode node, String key) {
        return node != null && node.hasNonNull(key) ? node.get(key).asText() : null;
    }

    private String getNullableStringFromArray(JsonNode arrayNode, String key) {
        if (arrayNode != null && arrayNode.isArray() && arrayNode.size() > 0) {
            JsonNode first = arrayNode.get(0);
            if (first != null && first.hasNonNull(key)) {
                return first.get(key).asText();
            }
        }
        return null;
    }

    private Integer getNullableInt(JsonNode node, String key) {
        return node != null && node.hasNonNull(key) ? node.get(key).asInt() : null;
    }

    private Double getNullableDouble(JsonNode node, String key) {
        return node != null && node.hasNonNull(key) ? node.get(key).asDouble() : null;
    }
}
