package com.spectre.payload.tools;


import java.time.LocalDateTime;

public class ImageUploadResponseDto {

    private Long id;
    private String title;
    private String filename;
    private String base64;
    private String uploader;
    private LocalDateTime uploadedAt;

    public ImageUploadResponseDto() {
    }

    public ImageUploadResponseDto(Long id, String title, String filename, String base64, String uploader, LocalDateTime uploadedAt) {
        this.id = id;
        this.title = title;
        this.filename = filename;
        this.base64 = base64;
        this.uploader = uploader;
        this.uploadedAt = uploadedAt;
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

    public String getUploader() {
        return uploader;
    }

    public LocalDateTime getUploadedAt() {
        return uploadedAt;
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

    public void setUploader(String uploader) {
        this.uploader = uploader;
    }

    public void setUploadedAt(LocalDateTime uploadedAt) {
        this.uploadedAt = uploadedAt;
    }
}