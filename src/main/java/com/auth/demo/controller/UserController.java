package com.auth.demo.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.auth.demo.apiResponces.ApiResponse;
import com.auth.demo.dto.AuthResponse.AccessToken;
import com.auth.demo.dto.AuthResponse.AuthResponse;
import com.auth.demo.dto.userDto.LoginDto;
import com.auth.demo.dto.userDto.UserCreateDTO;
import com.auth.demo.dto.userDto.UserResponseDTO;
import com.auth.demo.enums.ErrorMessage;
import com.auth.demo.enums.SuccessMessage;
import com.auth.demo.exception.AppException;
import com.auth.demo.services.interfaces.UserService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;

import org.springframework.http.*;

@RestController
@RequestMapping("api/v1/auth")
public class UserController {

    public final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping
    public ResponseEntity<ApiResponse<UserResponseDTO>> register(@RequestBody @Valid UserCreateDTO userCreateDTO) {

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new ApiResponse<>(userService.register(userCreateDTO), SuccessMessage.USER_CREATED.getMessage()));
    }

    @PostMapping("login")
    public ResponseEntity<ApiResponse<AccessToken>> login(@RequestBody @Valid LoginDto loginDot,
            HttpServletRequest request) {

        AuthResponse auth = userService.login(loginDot, request);
        ResponseCookie cookie = ResponseCookie.from("refreshToken", auth.getRefreshToken())
                .httpOnly(true)
                .secure(true)
                .path("/")
                .maxAge(7 * 24 * 60 * 60)
                .sameSite("Strict")
                .build();

        return ResponseEntity.ok()
                .header(HttpHeaders.SET_COOKIE, cookie.toString())
                .body(new ApiResponse<>(new AccessToken(auth.getAccessToken()),
                        SuccessMessage.LOGIN_SUCCESS.getMessage()));
    }

    @GetMapping("refresh-token")
    public ResponseEntity<ApiResponse<AuthResponse>> refreshAccessToken(
            @CookieValue(name = "refreshToken", required = false) String refreshToken) {

        if (refreshToken == null) {
            throw AppException.unauthorized(ErrorMessage.REFRESH_TOKEN_MISSING.getMessage());
        }

        return ResponseEntity.ok(new ApiResponse<>(userService.refreshAccessToken(refreshToken),
                SuccessMessage.TOKEN_REFRESHED.getMessage()));
    }
}
