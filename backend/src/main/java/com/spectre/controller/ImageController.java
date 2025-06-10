package com.spectre.controller;

import com.spectre.payload.tools.ImageResponseDto;
import com.spectre.payload.tools.ImageUploadResponseDto;
import com.spectre.security.services.tools.ImageService;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api/images")
@CrossOrigin(origins = "*")
public class ImageController {

    @Autowired
    private ImageService imageService;

    @GetMapping
    public List<ImageResponseDto> getAllImages() {
        return imageService.getAllImages();
    }

    @PostMapping
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public ResponseEntity<ImageUploadResponseDto> uploadImage(
            @RequestParam("file") MultipartFile file,
            @RequestParam(value = "title", required = false) String title,
            Authentication auth) throws IOException {

        String username = auth.getName();
        ImageUploadResponseDto response = imageService.uploadImage(file, username, title);
        return ResponseEntity.ok(response);
    }   

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public ResponseEntity<?> deleteImage(@PathVariable Long id, Authentication auth) {
        String username = auth.getName();
        boolean isAdmin = auth.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"));

        imageService.deleteImage(id, username, isAdmin);
        return ResponseEntity.ok("Image deleted");
    }
}
