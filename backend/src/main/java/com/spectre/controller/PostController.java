package com.spectre.controller;

import com.spectre.model.Post;
import com.spectre.payload.response.MessageResponse;
import com.spectre.payload.tools.PostRequestDto;
import com.spectre.payload.tools.PostResponseDto;
import com.spectre.security.services.tools.PostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/posts")
public class PostController {

    @Autowired
    private PostService postService;

    @GetMapping
    public ResponseEntity<List<PostResponseDto>> getAllPosts() {
        return ResponseEntity.ok(postService.getAllPostDtos());
    }
    

    @GetMapping("/{id}")
    public ResponseEntity<PostResponseDto> getPostById(@PathVariable Long id) {
        return ResponseEntity.ok(postService.getPostDtoById(id));
    }

    @PostMapping
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public ResponseEntity<PostResponseDto> createPost(@RequestBody PostRequestDto request, Authentication authentication) {
        String username = authentication.getName();
        return ResponseEntity.ok(postService.createPost(request, username));
    }


    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public ResponseEntity<?> deletePost(@PathVariable Long id, Authentication authentication) {
        postService.deletePost(id, authentication.getName());
        return ResponseEntity.ok(new MessageResponse("Post deleted successfully"));
    }

    @DeleteMapping("/admin/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> adminDeletePost(@PathVariable Long id) {
        postService.adminDeletePost(id);
        return ResponseEntity.ok(new MessageResponse("Post deleted by admin"));
    }

}



