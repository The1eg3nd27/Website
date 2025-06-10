package com.spectre.security.services.tools;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.spectre.payload.tools.DiscordEventDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Slf4j
@Service
public class DiscordEventService {

    @Value("${discord.bot.token}")
    private String botToken;

    @Value("${discord.guild.id}")
    private String guildId;

    private static final String DISCORD_EVENTS_API = "https://discord.com/api/v10/guilds/%s/scheduled-events";

    private final ObjectMapper objectMapper = new ObjectMapper();
    private final HttpClient httpClient = HttpClient.newHttpClient();

    public List<DiscordEventDto> getScheduledEvents() {
        try {
            String url = String.format(DISCORD_EVENTS_API, guildId);

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(url))
                    .header("Authorization", "Bot " + botToken)
                    .header("Accept", "application/json")
                    .GET()
                    .build();

            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
            log.info("✅ Discord API Response Status: {}", response.statusCode());

            if (response.statusCode() != 200) {
                log.error("❌ Discord API error: {}", response.body());
                return Collections.emptyList();
            }

            JsonNode root = objectMapper.readTree(response.body());
            List<DiscordEventDto> result = new ArrayList<>();

            for (JsonNode node : root) {
                String name = node.path("name").asText(null);
                String description = node.path("description").asText(null);
                String start = node.path("scheduled_start_time").asText(null);
                String end = node.path("scheduled_end_time").asText(null);
                String location = node.path("entity_metadata").path("location").asText(null);

                if (name == null || start == null) {
                    log.warn("⚠️ Skipping event with missing required fields: {}", node.toPrettyString());
                    continue;
                }

                DiscordEventDto dto = DiscordEventDto.builder()
                        .name(name)
                        .description(description)
                        .startTime(start)
                        .endTime(end)
                        .location(location)
                        .build();

                result.add(dto);
            }

            return result;

        } catch (Exception e) {
            log.error("❌ Failed to fetch Discord events", e);
            return Collections.emptyList();
        }
    }
}
