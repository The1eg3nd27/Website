package com.spectre.security.services;

import com.spectre.model.ERole;
import com.spectre.model.Role;
import com.spectre.model.User;
import com.spectre.repository.RoleRepository;
import com.spectre.repository.UserRepository;
import com.spectre.security.jwt.JwtUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class AuthService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private JwtUtils jwtUtils;

    @Value("${discord.client.id}")
    private String clientId;

    @Value("${discord.client.secret}")
    private String clientSecret;

    @Value("${discord.redirect.uri}")
    private String redirectUri;

    public String handleDiscordOAuth2Login(String code) {
        RestTemplate restTemplate = new RestTemplate();
    
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
    
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("client_id", clientId);
        params.add("client_secret", clientSecret);
        params.add("grant_type", "authorization_code");
        params.add("code", code);
        params.add("redirect_uri", redirectUri);
        params.add("scope", "identify guilds guilds.members.read");
    
        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(params, headers);
    
        ResponseEntity<Map> response = restTemplate.postForEntity(
            "https://discord.com/api/oauth2/token", request, Map.class);
    
        String accessToken = (String) response.getBody().get("access_token");

    
        HttpHeaders userHeaders = new HttpHeaders();
        userHeaders.setBearerAuth(accessToken);
        HttpEntity<String> userRequest = new HttpEntity<>(userHeaders);

        ResponseEntity<Map> userResponse = restTemplate.exchange(
            "https://discord.com/api/users/@me",
            HttpMethod.GET,
            userRequest,
            Map.class
        );
    
        Map<String, Object> userData = userResponse.getBody();
        String discordId = (String) userData.get("id");
        String username = (String) userData.get("username");
        String avatar = (String) userData.get("avatar");
    
        String guildId = "1120402155908632657"; 
        ResponseEntity<Map> memberResponse = restTemplate.exchange(
            "https://discord.com/api/users/@me/guilds/" + guildId + "/member",
            HttpMethod.GET,
            userRequest,
            Map.class
        );
    
        Map<String, Object> memberData = memberResponse.getBody();
        List<String> discordRoles = (List<String>) memberData.get("roles");
    
        ERole mappedRole;
        if (discordRoles.contains("1120403312185974814") || discordRoles.contains("1120404614328635473")) {
            mappedRole = ERole.ROLE_ADMIN;
        } else if (discordRoles.contains("1120403755658129418")) {
            mappedRole = ERole.ROLE_USER;
        } else {
            mappedRole = ERole.ROLE_GUEST;
        }
        

    
        Optional<User> existingUser = userRepository.findByDiscordId(discordId);
        User user = existingUser.orElseGet(() -> {
            User newUser = new User();
            newUser.setDiscordId(discordId);
            newUser.setUsername(username);
            newUser.setAvatar(avatar);
    
            Role defaultRole = roleRepository.findByName(mappedRole)
                .orElseThrow(() -> new RuntimeException("Error: Role not found."));
            newUser.setRoles(Collections.singleton(defaultRole));
    
            return userRepository.save(newUser);
        });
    
        return jwtUtils.generateTokenFromDiscordData(discordId, username, avatar);
    }
}
    
