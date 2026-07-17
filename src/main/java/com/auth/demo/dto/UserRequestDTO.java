package com.auth.demo.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Min;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserRequestDTO {

    @Email(message = "Invalid email format")
    private String email;

    @Min(value = 0, message = "Age must be positive")
    private Integer age;
}