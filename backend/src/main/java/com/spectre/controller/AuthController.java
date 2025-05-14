package com.spectre.controller;

import java.io.IOException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.spectre.model.ERole;
import com.spectre.model.RefreshToken;
import com.spectre.model.Role;
import com.spectre.model.User;
import com.spectre.payload.request.LoginRequest;
import com.spectre.payload.request.SignupRequest;
import com.spectre.payload.request.TokenRefreshRequest;
import com.spectre.payload.response.JwtResponse;
import com.spectre.payload.response.MessageResponse;
import com.spectre.payload.response.TokenRefreshResponse;
import com.spectre.repository.RoleRepository;
import com.spectre.repository.UserRepository;
import com.spectre.security.jwt.JwtUtils;
import com.spectre.security.jwt.TokenRefreshException;
import com.spectre.security.services.AuthService;
import com.spectre.security.services.RefreshTokenService;
import com.spectre.security.services.UserDetailsImpl;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
//@RequestMapping("/api/auth")
public class AuthController {
  @Autowired
  AuthenticationManager authenticationManager;

  @Autowired
  UserRepository userRepository;

  @Autowired
  RoleRepository roleRepository;

  @Autowired
  PasswordEncoder encoder;

  @Autowired
  JwtUtils jwtUtils;
  
  @Autowired
  private RefreshTokenService refreshTokenService;

  @Value("${frontend.oauth2.redirect}")
  private String frontendRedirectUrl;

  @Autowired
  private AuthService authService;

  @GetMapping("/discord/callback")
  public void handleDiscordCallback(@RequestParam String code, HttpServletResponse response) throws IOException {
      String jwtToken = authService.handleDiscordOAuth2Login(code);
      response.sendRedirect("http://localhost:3000/success?token=" + jwtToken);
  }


  @GetMapping("/discord")
  public void redirectToDiscord(HttpServletResponse response) throws IOException {
    String discordAuthUrl = "https://discord.com/api/oauth2/authorize"
        + "?client_id=1371876774803144774"
        + "&redirect_uri=http://localhost:8081/discord/callback"
        + "&response_type=code"
        + "&scope=identify%20guilds%20guilds.members.read";

    response.sendRedirect(discordAuthUrl);
}
    

  @PostMapping("/signin")
  public ResponseEntity<?> authenticateUser(@Valid @RequestBody LoginRequest loginRequest) {

    Authentication authentication = authenticationManager.authenticate(
        new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));

    SecurityContextHolder.getContext().setAuthentication(authentication);
    String jwt = jwtUtils.generateJwtToken(authentication);
    
    UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();    
    List<String> roles = userDetails.getAuthorities().stream()
        .map(item -> item.getAuthority())
        .collect(Collectors.toList());

    return ResponseEntity.ok(new JwtResponse(jwt,
                         userDetails.getId(), 
                         userDetails.getUsername(), 
                         null, 
                         roles));
  }

  @PostMapping("/refreshtoken")
    public ResponseEntity<?> refreshToken(@Valid @RequestBody TokenRefreshRequest request) {
        String requestToken = request.getRefreshToken();

        return refreshTokenService.findByToken(requestToken)
                .map(refreshTokenService::verifyExpiration)
                .map(RefreshToken::getUser)
                .map(user -> {
                    String role = user.getRoles().stream()
                            .findFirst()
                            .map(r -> r.getName().name())
                            .orElse("ROLE_GUEST");

                            String token = jwtUtils.generateToken(user.getId().toString(), user.getUsername(), role);
                            return ResponseEntity.ok(new TokenRefreshResponse(token, requestToken));
                })
                .orElseThrow(() -> new TokenRefreshException(requestToken, "Refresh token is not in database!"));
    }



  @PostMapping("/signup")
  public ResponseEntity<?> registerUser(@Valid @RequestBody SignupRequest signUpRequest) {
    if (userRepository.existsByUsername(signUpRequest.getUsername())) {
      return ResponseEntity
          .badRequest()
          .body(new MessageResponse("Error: Username is already taken!"));
    }

    if (userRepository.existsByEmail(signUpRequest.getEmail())) {
      return ResponseEntity
          .badRequest()
          .body(new MessageResponse("Error: Email is already in use!"));
    }

    User user = new User(signUpRequest.getUsername(), 
               signUpRequest.getEmail(),
               encoder.encode(signUpRequest.getPassword()));

    Set<String> strRoles = signUpRequest.getRole();
    Set<Role> roles = new HashSet<>();

    if (strRoles == null) {
      Role userRole = roleRepository.findByName(ERole.ROLE_USER)
          .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
      roles.add(userRole);
    } else {
      strRoles.forEach(role -> {
        switch (role) {
        case "admin":
          Role adminRole = roleRepository.findByName(ERole.ROLE_ADMIN)
              .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
          roles.add(adminRole);

          break;
        case "mod":
          Role modRole = roleRepository.findByName(ERole.ROLE_MODERATOR)
              .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
          roles.add(modRole);

          break;
        default:
          Role userRole = roleRepository.findByName(ERole.ROLE_USER)
              .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
          roles.add(userRole);
        }
      });
    }

    String role = user.getRoles().stream()
      .findFirst()
      .map(r -> r.getName().name())  
      .orElse("ROLE_GUEST");

    userRepository.save(user);

    return ResponseEntity.ok(new MessageResponse("User registered successfully!"));
  }
  


}