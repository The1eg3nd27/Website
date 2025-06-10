package com.spectre.payload.tools;

import lombok.Builder;
import lombok.Getter;

import java.time.Instant;

@Getter
@Builder
public class ImageResponseDto {
    private Long id;
    private String title;
    private String filename;
    private Instant uploadedAt;
    private String uploaderName;
}
