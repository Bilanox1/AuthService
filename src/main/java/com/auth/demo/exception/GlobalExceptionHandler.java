package com.auth.demo.exception;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.resource.NoResourceFoundException;

import com.auth.demo.apiResponces.ApiResponse;
import com.auth.demo.apiResponces.ErrorDetail;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(NoResourceFoundException.class)
    public ResponseEntity<ApiResponse<Object>> handleNotFound(NoResourceFoundException ex) {

        return buildErrorResponse(
                "Resource not found",
                List.of(ex.getMessage()),
                HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiResponse<Object>> handleValidation(MethodArgumentNotValidException ex) {

        List<ErrorDetail> errors = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(fieldError -> new ErrorDetail(
                        fieldError.getField(),
                        fieldError.getDefaultMessage()))
                .toList();

        return buildErrorResponse(
                "Validation failed",
                errors, 
                HttpStatus.BAD_REQUEST);
    }


    @ExceptionHandler(org.springframework.http.converter.HttpMessageNotReadableException.class)
    public ResponseEntity<ApiResponse<Object>> handleHttpMessageNotReadable(HttpMessageNotReadableException ex) {

        String errorMessage = "Request body is missing or malformed";

        return buildErrorResponse(
                errorMessage,
                List.of(ex.getLocalizedMessage()),
                HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(AppException.class)
    public ResponseEntity<ApiResponse<Object>> handleAppException(AppException ex) {

        if(ex.getField() == null) {
            return buildErrorResponse(
                    "Bad request",
                    List.of(ex.getMessage()),
                    HttpStatus.valueOf(ex.getCode()));
        }

        ErrorDetail error = new ErrorDetail(ex.getField(), ex.getMessage());

        return buildErrorResponse(
                "Bad request",
                List.of(error),
                HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponse<Object>> handleGeneric(Exception ex) {

        return buildErrorResponse(
                "Internal server error",
                List.of(ex.getMessage()),
                HttpStatus.INTERNAL_SERVER_ERROR);
    }

    private ResponseEntity<ApiResponse<Object>> buildErrorResponse(
            String message,
            List<?> errors,
            HttpStatus status) {

        ApiResponse<Object> response = new ApiResponse<>(
                message,
                errors);

        return ResponseEntity.status(status).body(response);
    }

}