package com.auth.demo.services.impl;

import java.util.UUID;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.auth.demo.Entity.User;
import com.auth.demo.dto.AuthResponse.AuthResponse;
import com.auth.demo.dto.userDto.*;
import com.auth.demo.enums.ErrorMessage;
import com.auth.demo.exception.AppException;
import com.auth.demo.repository.UserRepository;
import com.auth.demo.security.JwtService;
import com.auth.demo.services.interfaces.UserService;
import com.github.f4b6a3.uuid.UuidCreator;

import jakarta.servlet.http.HttpServletRequest;

@Service
public class UserServicesImpl implements UserService {

        private final UserRepository userRepository;
        private final PasswordEncoder passwordEncoder;
        private final JwtService jwtService;
        private final UserSessionsServiceImpl userSessionsService;

        public UserServicesImpl(UserRepository userRepository,
                        PasswordEncoder passwordEncoder,
                        JwtService jwtService, UserSessionsServiceImpl userSessionsService) {
                this.userRepository = userRepository;
                this.passwordEncoder = passwordEncoder;
                this.jwtService = jwtService;
                this.userSessionsService = userSessionsService;
        }

        public UserResponseDTO register(UserCreateDTO dto) {

                if (userRepository.existsByEmail(dto.email())) {
                        throw AppException.badRequest(ErrorMessage.EMAIL_ALREADY_USED.getMessage(), "email");
                }

                User user = new User();
                user.setFirstname(dto.firstname());
                user.setLastname(dto.lastname());
                user.setEmail(dto.email());
                user.setPassword(passwordEncoder.encode(dto.password()));

                userRepository.save(user);

                return new UserResponseDTO(
                                user.getPublicId(),
                                user.getFirstname(),
                                user.getLastname(),
                                user.getEmail());
        }

        public AuthResponse login(LoginDto dto, HttpServletRequest request) {

                User user = userRepository.findByEmail(dto.email())
                                .orElseThrow(() -> AppException
                                                .badRequest(ErrorMessage.INVALID_CREDENTIALS.getMessage()));

                if (!passwordEncoder.matches(dto.password(), user.getPassword())) {
                        throw AppException.badRequest(ErrorMessage.INVALID_CREDENTIALS.getMessage());
                }

                String accessToken = jwtService.accessToken(user.getEmail(), user.getPublicId());

                String refreshToken = UuidCreator.getTimeOrderedEpoch().toString();
                Boolean sessionCreated = userSessionsService.createSession(user, request, refreshToken);

                if (!sessionCreated) {
                        throw AppException.internalServerError(ErrorMessage.SESSION_CREATION_FAILED.getMessage());
                }

                return new AuthResponse(accessToken, refreshToken);
        }

        public AuthResponse refreshAccessToken(String refreshToken) {

                if (!userSessionsService.isSessionValid(refreshToken)) {
                        throw AppException.unauthorized(ErrorMessage.SESSION_INVALID.getMessage());
                }

                User user = userSessionsService.getUserByRefreshToken(refreshToken);
                String newAccessToken = jwtService.accessToken(user.getEmail(), user.getPublicId());

                return new AuthResponse(newAccessToken, null);
        }

        public UserResponseDTO getUserByPublicId(UUID publicId) {

                User user = userRepository.findByPublicId(publicId)
                                .orElseThrow(() -> AppException.notFound(ErrorMessage.USER_NOT_FOUND.getMessage()));

                return new UserResponseDTO(
                                user.getPublicId(),
                                user.getFirstname(),
                                user.getLastname(),
                                user.getEmail());
        }
}