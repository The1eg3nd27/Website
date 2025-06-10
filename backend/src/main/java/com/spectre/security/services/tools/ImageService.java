package com.spectre.security.services.tools;

import com.spectre.model.Image;
import com.spectre.model.User;
import com.spectre.payload.tools.ImageResponseDto;
import com.spectre.payload.tools.ImageUploadResponseDto;
import com.spectre.repository.ImageRepository;
import com.spectre.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ImageService {

    private final ImageRepository imageRepository;
    private final UserRepository userRepository;

    @Transactional
    public ImageUploadResponseDto upload(MultipartFile file, String title) throws IOException {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userRepository.findByUsername(username).orElseThrow();

        Image image = new Image();
        image.setTitle(title);
        image.setFilename(file.getOriginalFilename());
        image.setContent(file.getBytes());
        image.setUploader(user);

        image = imageRepository.save(image);
        return new ImageUploadResponseDto(image.getId(), "Uploaded successfully");
    }

    @Transactional(readOnly = true)
    public List<ImageResponseDto> getAll() {
        return imageRepository.findAllByOrderByUploadedAtDesc().stream()
                .map(img -> ImageResponseDto.builder()
                        .id(img.getId())
                        .title(img.getTitle())
                        .filename(img.getFilename())
                        .uploadedAt(img.getUploadedAt())
                        .uploaderName(img.getUploader().getUsername())
                        .build())
                .toList();
    }
}
