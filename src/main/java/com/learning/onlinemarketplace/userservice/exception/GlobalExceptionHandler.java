package com.learning.onlinemarketplace.userservice.exception;

import com.learning.onlinemarketplace.userservice.dto.response.ApiResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.Objects;

@ControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(value = RuntimeException.class)
    ResponseEntity<ApiResponse<String>> handleRuntimeException(RuntimeException e) {
        var apiResponse = new ApiResponse<String>();
        apiResponse.setSuccess(false);
        apiResponse.setMessage(e.getMessage());
        return ResponseEntity.badRequest().body(apiResponse);
    }

    @ExceptionHandler(value = MethodArgumentNotValidException.class)
    ResponseEntity<ApiResponse<String>> handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {
        var apiResponse = new ApiResponse<String>();
        apiResponse.setSuccess(false);
        apiResponse.setMessage(Objects.requireNonNull(e.getBindingResult().getFieldError()).getDefaultMessage());
        return ResponseEntity.badRequest().body(Objects.requireNonNull(apiResponse));
    }
}
