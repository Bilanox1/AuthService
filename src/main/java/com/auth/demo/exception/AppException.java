package com.auth.demo.exception;

import lombok.Getter;

@Getter
public class AppException extends RuntimeException {

    private final int code;
    private final String message;
    private final String field;

    private AppException(int code, String message, String field) {
        super(message);
        this.code = code;
        this.message = message;
        this.field = field;
    }

    public static AppException badRequest(String message) {
        return new AppException(400, message, null);
    }

    public static AppException badRequest(String message, String field) {
        return new AppException(400, message, field);
    }

    public static AppException conflict(String message, String field) {
        return new AppException(409, message, field);
    }

    public static AppException notFound(String message) {
        return new AppException(404, message, null);
    }

    public static AppException internalServerError(String message) {
        return new AppException(500, message, null);
    }

    public static AppException unauthorized(String message) {
        return new AppException(401, message, null);
    }

}