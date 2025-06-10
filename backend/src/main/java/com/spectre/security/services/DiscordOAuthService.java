package com.spectre.security.services;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.spectre.payload.tools.DiscordTokenResponse;
import com.spectre.payload.tools.DiscordUserInfoResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;


@Slf4j
@Service
@RequiredArgsConstructor
public class DiscordOAuthService {

    @Value("${discord.client.id}")
    private String clientId;

    @Value("${discord.client.secret}")
    private String clientSecret;

    @Value("${spectre.app.discord.redirectUri}")
    private String redirectUri;


    @Value("${discord.guild.id}")
    private String guildId;


    private final RestTemplate restTemplate = new RestTemplate();
    private final ObjectMapper objectMapper = new ObjectMapper();

    public DiscordTokenResponse exchangeCodeForToken(String code) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        MultiValueMap<String, String> formData = new LinkedMultiValueMap<>();
        formData.add("client_id", clientId);
        formData.add("client_secret", clientSecret);
        formData.add("grant_type", "authorization_code");
        formData.add("code", code);
        formData.add("redirect_uri", redirectUri); 
   
        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(formData, headers);

        ResponseEntity<DiscordTokenResponse> response = restTemplate.postForEntity(
            "https://discord.com/api/oauth2/token",
            request,
            DiscordTokenResponse.class
        );
    
        return response.getBody();
    }


    public DiscordUserInfoResponse getGuildMemberInfo(String accessToken) {
        String url = "https://discord.com/api/users/@me/guilds/" + guildId + "/member";
    
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(accessToken);
    
        HttpEntity<Void> entity = new HttpEntity<>(headers);
    
        ResponseEntity<DiscordUserInfoResponse> response = restTemplate.exchange(
            url, HttpMethod.GET, entity, DiscordUserInfoResponse.class
        );
    
        return response.getBody();
    }
    
}
