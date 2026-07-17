package com.auth.demo.dto.userDto;

import com.auth.demo.ValidationMessages.ValidationMessages;

import jakarta.validation.constraints.*;

public record UserCreateDTO(

                @NotBlank(message = ValidationMessages.FIRST_NAME_REQUIRED) @Size(max = 50, message = ValidationMessages.FIRST_NAME_SIZE) String firstname,

                @NotBlank(message = ValidationMessages.LAST_NAME_REQUIRED) @Size(max = 50, message = ValidationMessages.LAST_NAME_SIZE) String lastname,

                @NotBlank(message = ValidationMessages.EMAIL_REQUIRED) @Email(message = ValidationMessages.EMAIL_INVALID) String email,

                @NotBlank(message = ValidationMessages.PASSWORD_REQUIRED) @Size(min = 8, message = ValidationMessages.PASSWORD_TOO_SHORT) String password) {
}