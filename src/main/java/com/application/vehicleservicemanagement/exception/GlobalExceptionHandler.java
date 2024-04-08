package com.application.vehicleservicemanagement.exception;

import com.application.vehicleservicemanagement.dto.ApiResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ApiResponse> resourceNotFoundExceptionHandler(ResourceNotFoundException ex) {
        return new ResponseEntity<>(ApiResponse.builder()
                .message(ex.getMessage())
                .status("Unsuccessful")
                .build(), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(CommonException.class)
    public ResponseEntity<ApiResponse> commonExceptionHandler(CommonException exception) {
        return new ResponseEntity<>(ApiResponse.builder()
                .message(exception.getMessage())
                .status("Unsuccessful")
                .build(), HttpStatus.EXPECTATION_FAILED);
    };
}
