package com.auth.demo.enums;

public enum MessageCode {

    USER_FETCHED("User fetched successfully"),
    USERS_FETCHED("Users fetched successfully"),
    USER_CREATED("User created successfully"),
    USER_UPDATED("User updated successfully"),
    USER_DELETED("User deleted successfully"),
    USER_NOT_FOUND("User not found"),
    INTERNAL_ERROR("Internal server error");

    private final String message;

    MessageCode(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}