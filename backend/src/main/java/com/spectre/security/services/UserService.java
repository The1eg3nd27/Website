package com.spectre.security.services;

import com.spectre.model.ERole;
import com.spectre.model.Role;
import com.spectre.model.User;
import com.spectre.payload.tools.DiscordBasicUserInfo;
import com.spectre.payload.tools.DiscordUserInfoResponse;
import com.spectre.payload.tools.UserSummaryDto;
import com.spectre.repository.RoleRepository;
import com.spectre.repository.UserRepository;
import com.spectre.security.util.RoleMapper;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class UserService {

    private final UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Transactional(readOnly = true)
    public UserSummaryDto getCurrentUser() {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
        return RoleMapper.toDto(user);
    }
    public User processDiscordLogin(DiscordUserInfoResponse userInfo) {
        DiscordBasicUserInfo discordUser = userInfo.getUser(); // ✅ renamed to avoid confusion
    
        String discordId = discordUser.getId();
        String username = discordUser.getUsername();
    
        if (username == null || username.isBlank()) {
            username = discordUser.getGlobalName();
        }
        if (username == null || username.isBlank()) {
            username = "UnknownSpectre";
        }
    
        String avatarHash = discordUser.getAvatarHash();
        List<String> discordRoleIds = userInfo.getRoles();
    
        ERole desiredRole;
        if (discordRoleIds.contains("1120403312185974814") || discordRoleIds.contains("1120404614328635473")) {
            desiredRole = ERole.ROLE_ADMIN;
        } else if (discordRoleIds.contains("1120403755658129418")) {
            desiredRole = ERole.ROLE_MEMBER;
        } else {
            desiredRole = ERole.ROLE_GUEST;
        }
    
        Role role = roleRepository.findByName(desiredRole)
            .orElseThrow(() -> new RuntimeException("❌ Role not found in DB: " + desiredRole));
    
        Optional<User> existing = userRepository.findByDiscordId(discordId);
        User user = existing.orElseGet(User::new);
    
        user.setDiscordId(discordId);
        user.setUsername(username);
    
        if (discordId != null && avatarHash != null && !avatarHash.isBlank()) {
            user.setAvatar("https://cdn.discordapp.com/avatars/" + discordId + "/" + avatarHash + ".png");
        } else {
            user.setAvatar("https://cdn.discordapp.com/embed/avatars/0.png");
        }
    
        user.setRoles(Set.of(role));
    
        return userRepository.save(user);
    }
    

}
