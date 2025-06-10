package com.spectre.payload.tools;


import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class DiscordBasicUserInfo {
    private String id;

    private String username;

    @JsonProperty("global_name")
    private String globalName;

    @JsonProperty("avatar")
    private String avatarHash;
}
