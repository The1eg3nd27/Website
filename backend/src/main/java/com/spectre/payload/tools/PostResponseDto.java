package com.spectre.payload.tools;

import com.spectre.model.Post;

import java.time.LocalDateTime;

public class PostResponseDto {
    private Long id;
    private String title;
    private String content;
    private String author;
    private LocalDateTime createdAt;

    public PostResponseDto(Long id, String title, String content, String author, LocalDateTime createdAt) {
        this.id = id;
        this.title = title;
        this.content = content;
        this.author = author;
        this.createdAt = createdAt;
    }

    public static PostResponseDto fromEntity(Post post) {
        String username = (post.getAuthor() != null && post.getAuthor().getUsername() != null)
            ? post.getAuthor().getUsername()
            : "Unknown";
    
        return new PostResponseDto(
            post.getId(),
            post.getTitle(),
            post.getContent(),
            username,
            post.getCreatedAt()
        );
    }
    

    public Long getId() { return id; }
    public String getTitle() { return title; }
    public String getContent() { return content; }
    public String getAuthor() { return author; }
    public LocalDateTime getCreatedAt() { return createdAt; }
}
