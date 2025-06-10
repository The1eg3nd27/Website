package com.spectre.controller;

import com.spectre.payload.tools.UserSummaryDto;
import com.spectre.security.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api/user")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping("/me")
    @PreAuthorize("hasAnyRole('GUEST', 'MEMBER', 'ADMIN')")
    public ResponseEntity<UserSummaryDto> getCurrentUser() {
        return ResponseEntity.ok(userService.getCurrentUser());
    }
}
