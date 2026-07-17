package com.auth.demo.dto.userDto;

import java.util.UUID;

public record UserResponseDTO(
        UUID publicId,
        String firstname,
        String lastname,
        String email) {
}