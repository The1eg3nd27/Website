package com.spectre.security.services;

import com.spectre.model.User;
import com.spectre.payload.response.JwtResponse;
import com.spectre.repository.UserRepository;
import com.spectre.security.jwt.JwtUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class AuthService {

    private final AuthenticationManager authenticationManager;
    private final JwtUtils jwtUtils;
    private final RefreshTokenService refreshTokenService;
    private final UserRepository userRepository;

    public JwtResponse authenticate(String username) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(username, ""));

        String jwt = jwtUtils.generateJwtToken(authentication);
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();

        String refreshToken = refreshTokenService.createRefreshToken(userDetails.getId()).getToken();

        User user = userRepository.findByUsername(username).orElseThrow();

        return JwtResponse.builder()
                .token(jwt)
                .refreshToken(refreshToken)
                .id(user.getId())
                .username(user.getUsername())
                .discordId(user.getDiscordId())
                .roles(user.getRoles().stream()
                        .map(role -> role.getName().name())
                        .collect(Collectors.toList()))
                .build();
    }
}
