package com.auth.demo.enums;

public enum ErrorMessage {

    INVALID_CREDENTIALS("Invalid credentials"),
    EMAIL_ALREADY_USED("Email already in use"),
    USER_NOT_FOUND("User not found"),
    SESSION_INVALID("Invalid or expired session"),
    SESSION_CREATION_FAILED("Failed to create user session"),
    REFRESH_TOKEN_MISSING("Refresh token is missing");

    private final String message;

    ErrorMessage(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}