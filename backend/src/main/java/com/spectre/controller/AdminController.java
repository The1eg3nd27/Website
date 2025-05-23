package com.spectre.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import com.spectre.payload.tools.UserSummaryDto;
import com.spectre.security.services.UserService;
import com.spectre.security.services.tools.ImageService;
import com.spectre.security.services.tools.PostService;


import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@RestController
@RequestMapping("/api/admin")
@CrossOrigin(origins = "*")
public class AdminController {

    @GetMapping("/dashboard")
    @PreAuthorize("hasRole('ADMIN')")
    public Map<String, Object> getAdminDashboard(Authentication auth) {
        Map<String, Object> data = new HashMap<>();
        data.put("message", "Willkommen ihr Schlingel! Hab das nur für euch hier gebaut also enjoy");
        data.put("admin", auth.getName());
        data.put("time", LocalDateTime.now());
        return data;
        
    }
    @Autowired
    private UserService userService;

    @Autowired
    private ImageService imageService;

    @Autowired
    private PostService postService;
    




    @GetMapping("/users")
    @PreAuthorize("hasRole('ADMIN')")
    public List<UserSummaryDto> getAllUsers() {
        return userService.getAllUsers();
    }
    @DeleteMapping("/images/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> deleteImageAsAdmin(@PathVariable Long id) {
        imageService.deleteImage(id, "admin", true); // Pass true for isAdmin
        return ResponseEntity.ok("✅ Image deleted by admin.");
    }


    @DeleteMapping("/posts/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> adminDeletePost(@PathVariable Long id) {
        postService.deletePost(id, "ADMIN_OVERRIDE");  // Force admin=true
        return ResponseEntity.ok().build();
    }


}
