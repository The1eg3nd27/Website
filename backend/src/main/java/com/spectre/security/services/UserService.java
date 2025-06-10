package com.spectre.security.services;

import com.spectre.model.User;
import com.spectre.payload.tools.UserSummaryDto;
import com.spectre.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.spectre.payload.tools.UserSummaryDto;


import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;

    public Optional<User> findByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    public User updateUser(User user) {
        return userRepository.save(user);
    }
    public User getUserByUsername(String username) {
        return userRepository.findByUsername(username)
                .orElse(null);
    }

    public List<UserSummaryDto> getAllUsers() {
    return userRepository.findAll().stream()
            .map(user -> new UserSummaryDto(
                user.getId(),
                user.getUsername(),
                user.getRoles().stream().findFirst().map(r -> r.getName().name()).orElse("UNKNOWN")
            ))
            .collect(Collectors.toList());
}

    
}
