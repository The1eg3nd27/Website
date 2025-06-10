package com.spectre.repository;

import com.spectre.model.Image;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ImageRepository extends JpaRepository<Image, Long> {
    List<Image> findAllByOrderByUploadedAtDesc();
}
