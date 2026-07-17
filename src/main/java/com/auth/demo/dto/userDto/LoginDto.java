package com.auth.demo.dto.userDto;

import com.auth.demo.ValidationMessages.ValidationMessages;

import jakarta.validation.constraints.*;

public record LoginDto(

                @NotBlank(message = ValidationMessages.EMAIL_REQUIRED) @Email(message = ValidationMessages.EMAIL_INVALID) String email,

                @NotBlank(message = ValidationMessages.PASSWORD_REQUIRED) @Size(min = 8, message = ValidationMessages.PASSWORD_TOO_SHORT) String password) {
}