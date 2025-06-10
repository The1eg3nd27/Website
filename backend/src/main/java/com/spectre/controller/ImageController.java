package com.spectre.controller;

import com.spectre.payload.tools.ImageResponseDto;
import com.spectre.payload.tools.ImageUploadResponseDto;
import com.spectre.security.services.tools.ImageService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api/images")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class ImageController {

    private final ImageService imageService;

    @PostMapping
    @PreAuthorize("hasAnyRole('MEMBER', 'ADMIN')")
    public ResponseEntity<ImageUploadResponseDto> uploadImage(
            @RequestParam("file") MultipartFile file,
            @RequestParam("title") String title) throws IOException {
        return ResponseEntity.ok(imageService.upload(file, title));
    }

    @GetMapping
    public ResponseEntity<List<ImageResponseDto>> getImages() {
        return ResponseEntity.ok(imageService.getAll());
    }
}
