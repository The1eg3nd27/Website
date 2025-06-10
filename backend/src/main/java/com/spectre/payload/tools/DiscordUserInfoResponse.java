package com.spectre.payload.tools;


import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class DiscordUserInfoResponse {
    private String id;
    @JsonProperty("username")
    private String username;
    private String discriminator;

    @JsonProperty("avatar")
    private String avatarHash;

    @JsonProperty("global_name")
    private String globalName;

    @JsonProperty("public_flags")
    private int publicFlags;

    @JsonProperty("flags")
    private int flags;

    @JsonProperty("banner")
    private String banner;

    @JsonProperty("accent_color")
    private Integer accentColor;

    @JsonProperty("locale")
    private String locale;

    @JsonProperty("roles")
    private List<String> roles;

    @JsonProperty("user")
    private DiscordBasicUserInfo user;




    @JsonProperty("mfa_enabled")
    private boolean mfaEnabled;
    public DiscordUserInfoResponse(String id, String username, String discriminator, String avatarHash) {
        this.id = id;
        this.username = username;
        this.discriminator = discriminator;
        this.avatarHash = avatarHash;
    }
    

    // Add more fields if needed from Discord response
}