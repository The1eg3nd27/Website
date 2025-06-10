package com.spectre.repository;

import com.spectre.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUsername(String username);
    Optional<User> findByDiscordId(String discordId);
    Boolean existsByUsername(String username);
}
