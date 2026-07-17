package com.auth.demo.services.interfaces;

import java.util.UUID;

import com.auth.demo.dto.AuthResponse.AuthResponse;
import com.auth.demo.dto.userDto.LoginDto;
import com.auth.demo.dto.userDto.UserCreateDTO;
import com.auth.demo.dto.userDto.UserResponseDTO;

import jakarta.servlet.http.HttpServletRequest;

public interface UserService {

    UserResponseDTO register(UserCreateDTO userCreateDTO);

    AuthResponse login(LoginDto loginDTO, HttpServletRequest request);

    UserResponseDTO getUserByPublicId(UUID publicId);

    AuthResponse refreshAccessToken(String refreshToken);

}