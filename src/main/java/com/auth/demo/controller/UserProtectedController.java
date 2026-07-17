package com.auth.demo.controller;

import org.springframework.web.bind.annotation.*;

import java.util.UUID;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;

import com.auth.demo.apiResponces.ApiResponse;
import com.auth.demo.dto.userDto.UserResponseDTO;
import com.auth.demo.services.interfaces.UserService;

@RestController
@RequestMapping("api/v1/users")
public class UserProtectedController {

    private final UserService userService;

    public UserProtectedController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("me")
    public ResponseEntity<ApiResponse<UserResponseDTO>> getMyUser(Authentication authentication) {

        String userId = authentication.getName();
        return ResponseEntity.ok(new ApiResponse<>(userService.getUserByPublicId(UUID.fromString(userId)),
                "User details retrieved"));
    }

    @GetMapping("test")
    public ResponseEntity<ApiResponse<String>> test(Authentication authentication) {
        return ResponseEntity.ok(new ApiResponse<>(
                "Hello, " + authentication.getName() + "! This is a protected endpoint.", "Test successful"));
    }

}