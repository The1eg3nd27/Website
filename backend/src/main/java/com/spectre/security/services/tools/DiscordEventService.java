package com.spectre.security.services.tools;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.spectre.payload.tools.DiscordEventDto;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@Service
public class DiscordEventService {

    @Value("${discord.bot.token}")
    private String botToken;

    @Value("${discord.guild.id}")
    private String guildId;

    private final RestTemplate restTemplate = new RestTemplate();

    public List<DiscordEventDto> fetchEvents() {
        List<DiscordEventDto> result = new ArrayList<>();
        String url = "https://discord.com/api/v10/guilds/" + guildId + "/scheduled-events";

        try {
            HttpHeaders headers = new HttpHeaders();
            headers.set("Authorization", "Bot " + botToken);

            HttpEntity<String> entity = new HttpEntity<>(headers);

            ResponseEntity<String> response = restTemplate.exchange(
                    url, HttpMethod.GET, entity, String.class);

            ObjectMapper mapper = new ObjectMapper();
            JsonNode root = mapper.readTree(response.getBody());

            for (JsonNode node : root) {
                result.add(DiscordEventDto.builder()
                        .name(node.path("name").asText())
                        .description(node.path("description").asText())
                        .startTime(node.path("scheduled_start_time").asText())
                        .channel(node.path("channel_id").asText())
                        .creator(node.path("creator") != null
                                ? node.path("creator").path("username").asText()
                                : "Unknown")
                        .build());
            }

        } catch (Exception e) {
            result.add(DiscordEventDto.builder()
                    .name("Error fetching events")
                    .description(e.getMessage())
                    .build());
        }

        return result;
    }
}
