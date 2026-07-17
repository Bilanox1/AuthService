package com.auth.demo.enums;

public enum SuccessMessage {

    USER_CREATED("User account has been successfully created"),
    LOGIN_SUCCESS("Authentication successful"),
    TOKEN_REFRESHED("Access token refreshed successfully"),
    USER_FETCHED("User retrieved successfully");

    private final String message;

    SuccessMessage(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}