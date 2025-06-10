package com.spectre.payload.tools;

import lombok.Builder;
import lombok.Data;

@Data
@Builder 
public class DiscordEventDto {
    private String name;
    private String description;
    private String startTime;
    private String endTime;
    private String location;
}
