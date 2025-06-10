package com.spectre.controller;

import com.spectre.payload.tools.PostRequestDto;
import com.spectre.payload.tools.PostResponseDto;
import com.spectre.security.services.tools.PostService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/posts")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class PostController {

    private final PostService postService;

    @GetMapping
    public ResponseEntity<List<PostResponseDto>> getAllPosts() {
        return ResponseEntity.ok(postService.getAllPosts());
    }

    @PostMapping
    @PreAuthorize("hasAnyRole('MEMBER', 'ADMIN')")
    public ResponseEntity<?> createPost(@Valid @RequestBody PostRequestDto dto) {
        postService.createPost(dto);
        return ResponseEntity.ok("Post created");
    }
    @DeleteMapping("/{id}")
    @PreAuthorize("@postSecurity.isOwner(#id) or hasRole('ADMIN')")
    public ResponseEntity<?> deletePost(@PathVariable Long id) {
        postService.deleteById(id);
        return ResponseEntity.ok("Post deleted.");
    }
}
