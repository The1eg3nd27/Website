package com.spectre.security.util;

import com.spectre.model.User;
import com.spectre.payload.tools.UserSummaryDto;

import java.util.stream.Collectors;

public class RoleMapper {

    public static UserSummaryDto toDto(User user) {
        return UserSummaryDto.builder()
                .id(user.getId())
                .username(user.getUsername())
                .discordId(user.getDiscordId())
                .avatar(user.getAvatar())
                .roles(user.getRoles().stream()
                    .map(role -> role.getName().name())
                    .collect(Collectors.toSet()))
                .build();
    }
}
