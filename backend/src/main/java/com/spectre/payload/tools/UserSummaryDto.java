package com.spectre.payload.tools;

import lombok.Builder;
import lombok.Data;

import java.util.Set;

@Data
@Builder
public class UserSummaryDto {
    private Long id;
    private String username;
    private String discordId;
    private String avatar;
    private Set<String> roles;
}
