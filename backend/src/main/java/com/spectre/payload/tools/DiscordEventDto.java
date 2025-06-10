package com.spectre.payload.tools;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class DiscordEventDto {
    private String name;
    private String description;
    private String startTime;
    private String channel;
    private String creator;
}
