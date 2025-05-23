package com.spectre.payload.tools;

import java.time.LocalDateTime;

public class ImageResponseDto {

    private Long id;
    private String title;
    private String filename;
    private String base64;
    private LocalDateTime uploadedAt;
    private String uploader;

    public ImageResponseDto() {
    }

    public ImageResponseDto(Long id, String title, String filename, String base64, LocalDateTime uploadedAt, String uploader) {
        this.id = id;
        this.title = title;
        this.filename = filename;
        this.base64 = base64;
        this.uploadedAt = uploadedAt;
        this.uploader = uploader;
    }

    public Long getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getFilename() {
        return filename;
    }

    public String getBase64() {
        return base64;
    }

    public LocalDateTime getUploadedAt() {
        return uploadedAt;
    }

    public String getUploader() {
        return uploader;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }

    public void setBase64(String base64) {
        this.base64 = base64;
    }

    public void setUploadedAt(LocalDateTime uploadedAt) {
        this.uploadedAt = uploadedAt;
    }

    public void setUploader(String uploader) {
        this.uploader = uploader;
    }
}
