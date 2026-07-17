package com.auth.demo.ValidationMessages;

public class ValidationMessages {
    // Common
    public static final String REQUIRED = "This field is required";
    public static final String INVALID_FORMAT = "Invalid format";

    // Name
    public static final String FIRST_NAME_REQUIRED = "First name is required";
    public static final String FIRST_NAME_SIZE = "First name must not exceed 50 characters";

    public static final String LAST_NAME_REQUIRED = "Last name is required";
    public static final String LAST_NAME_SIZE = "Last name must not exceed 50 characters";

    // Email
    public static final String EMAIL_REQUIRED = "Email is required";
    public static final String EMAIL_INVALID = "Email format is invalid";

    // Password
    public static final String PASSWORD_REQUIRED = "Password is required";
    public static final String PASSWORD_TOO_SHORT = "Password must be at least 8 characters";
    public static final String PASSWORD_TOO_LONG = "Password must not exceed 100 characters";

    // Optional security improvements
    public static final String PASSWORD_WEAK = "Password must contain letters, numbers and special characters";
}