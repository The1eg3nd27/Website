package com.spectre.security.services.tools;

import com.spectre.model.Image;
import com.spectre.model.User;
import com.spectre.payload.tools.ImageResponseDto;
import com.spectre.payload.tools.ImageUploadResponseDto;
import com.spectre.repository.ImageRepository;
import com.spectre.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.IIOImage;
import javax.imageio.ImageIO;
import javax.imageio.ImageWriteParam;
import javax.imageio.ImageWriter;
import javax.imageio.stream.ImageOutputStream;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Base64;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ImageService {

    @Autowired
    private ImageRepository imageRepository;

    @Autowired
    private UserRepository userRepository;

    public List<ImageResponseDto> getAllImages() {
        return imageRepository.findAll().stream()
            .map(image -> new ImageResponseDto(
                image.getId(),
                image.getTitle(),
                image.getFilename(),
                Base64.getEncoder().encodeToString(image.getContent()),
                image.getUploadedAt(),
                image.getUploader().getUsername()
            ))
            .collect(Collectors.toList());
    }

    @Transactional
    public ImageUploadResponseDto uploadImage(MultipartFile file, String uploaderUsername, String customTitle) throws IOException {
        Optional<User> uploaderOpt = userRepository.findByUsername(uploaderUsername);
        if (uploaderOpt.isEmpty()) {
            throw new IllegalArgumentException("Uploader not found");
        }
    
        byte[] compressedImage = compressToJpeg(file);
    
        Image image = new Image();
        image.setTitle(customTitle != null && !customTitle.isBlank() ? customTitle : file.getOriginalFilename());
        image.setFilename(file.getOriginalFilename()); 
        image.setContent(compressedImage);
        image.setUploadedAt(LocalDateTime.now());
        image.setUploader(uploaderOpt.get());
    
        Image saved = imageRepository.save(image);
    
        return new ImageUploadResponseDto(
                saved.getId(),
                saved.getTitle(),
                saved.getFilename(),
                Base64.getEncoder().encodeToString(saved.getContent()),
                saved.getUploader().getUsername(),
                saved.getUploadedAt()
        );
    }

    
    @Transactional
    public void deleteImage(Long id, String requesterUsername, boolean isAdmin) {
        Optional<Image> imageOpt = imageRepository.findById(id);
        if (imageOpt.isEmpty()) return;

        Image image = imageOpt.get();
        boolean isOwner = image.getUploader().getUsername().equals(requesterUsername);

        if (!isOwner && !isAdmin) {
            throw new SecurityException("Unauthorized to delete image.");
        }

        imageRepository.deleteById(id);
    }

    private byte[] compressToJpeg(MultipartFile file) throws IOException {
        BufferedImage originalImage = ImageIO.read(file.getInputStream());
    
        if (originalImage == null) {
            throw new IOException("Unsupported or corrupted image file.");
        }
    
        BufferedImage convertedImage = new BufferedImage(
                originalImage.getWidth(),
                originalImage.getHeight(),
                BufferedImage.TYPE_INT_RGB
        );
    
        convertedImage.getGraphics().drawImage(originalImage, 0, 0, java.awt.Color.WHITE, null);
    
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ImageWriter jpegWriter = ImageIO.getImageWritersByFormatName("jpeg").next();
        ImageWriteParam jpegParams = jpegWriter.getDefaultWriteParam();
    
        jpegParams.setCompressionMode(ImageWriteParam.MODE_EXPLICIT);
        jpegParams.setCompressionQuality(0.8f);
    
        try (ImageOutputStream ios = ImageIO.createImageOutputStream(baos)) {
            jpegWriter.setOutput(ios);
            jpegWriter.write(null, new IIOImage(convertedImage, null, null), jpegParams);
        }
    
        jpegWriter.dispose();
        return baos.toByteArray();
    }
    
}
