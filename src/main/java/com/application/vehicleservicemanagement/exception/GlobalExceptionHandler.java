package com.application.vehicleservicemanagement.exception;

import com.application.vehicleservicemanagement.dto.ApiResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<ApiResponse> badCredentialsExceptionHandler(BadCredentialsException e) {
        return new ResponseEntity<>(ApiResponse.builder()
                .message(e.getMessage())
                .status(HttpStatus.UNAUTHORIZED.name())
                .build(), HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ApiResponse> resourceNotFoundExceptionHandler(ResourceNotFoundException e) {
        return new ResponseEntity<>(ApiResponse.builder()
                .message(e.getMessage())
                .status(HttpStatus.NOT_FOUND.name())
                .build(), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(InvalidRoleException.class)
    public ResponseEntity<ApiResponse> invalidRoleExceptionHandler(InvalidRoleException e) {
        return new ResponseEntity<>(ApiResponse.builder()
                .message(e.getMessage())
                .status(HttpStatus.LOCKED.name())
                .build(), HttpStatus.LOCKED);
    }

    @ExceptionHandler(CommonException.class)
    public ResponseEntity<ApiResponse> commonExceptionHandler(CommonException e) {
        return new ResponseEntity<>(ApiResponse.builder()
                .message(e.getMessage())
                .status(HttpStatus.EXPECTATION_FAILED.name())
                .build(), HttpStatus.EXPECTATION_FAILED);
    }
}
