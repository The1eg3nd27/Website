package com.spectre.controller;

import com.spectre.model.User;
import com.spectre.payload.response.JwtResponse;
import com.spectre.security.jwt.JwtUtils;
import com.spectre.security.services.DiscordOAuthService;
import com.spectre.security.services.UserService;
import com.spectre.payload.tools.DiscordTokenResponse;
import com.spectre.payload.tools.DiscordUserInfoResponse;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/discord")
@RequiredArgsConstructor
public class AuthController {

    private final DiscordOAuthService discordOAuthService;
    private final JwtUtils jwtUtils;
    private final UserService userService;

    @Value("${spectre.app.discord.redirectUri}")
    private String redirectUri;

    @Value("${discord.client.id}")
    private String clientId;

    @GetMapping
    public void redirectToDiscord(HttpServletResponse response) throws IOException {
        String discordAuthUrl = UriComponentsBuilder.fromHttpUrl("https://discord.com/oauth2/authorize")
                .queryParam("client_id", clientId)
                .queryParam("redirect_uri", redirectUri)
                .queryParam("response_type", "code")
                .queryParam("scope", "identify guilds")
                .build()
                .toString();
        response.sendRedirect(discordAuthUrl);
    }

    @GetMapping("/callback")
    public void handleCallback(@RequestParam("code") String code, HttpServletResponse response) throws IOException {
        DiscordTokenResponse tokenResponse = discordOAuthService.exchangeCodeForToken(code);
        String accessToken = tokenResponse.getAccessToken();
        DiscordUserInfoResponse userInfo = discordOAuthService.getGuildMemberInfo(accessToken);


        User user = userService.processDiscordLogin(userInfo);
        String jwt = jwtUtils.generateJwtToken(user, userInfo.getRoles());


        String frontendRedirectUrl = "http://localhost:3000/login/success?token=" + jwt;
        response.sendRedirect(frontendRedirectUrl);
    }


    @GetMapping("/check")
    public ResponseEntity<String> checkAuth() {
        return ResponseEntity.ok("User is authenticated");
    }
}
