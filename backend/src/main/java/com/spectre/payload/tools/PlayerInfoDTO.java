package com.spectre.payload.tools;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class PlayerInfoDTO {
    private String handle;
    private String organization;
    private String rank;
    private String bio;
    private String image;
    private String pageUrl;
}
