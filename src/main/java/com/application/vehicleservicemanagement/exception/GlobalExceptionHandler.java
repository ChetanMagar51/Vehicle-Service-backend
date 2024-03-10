package com.application.vehicleservicemanagement.exception;

import com.application.vehicleservicemanagement.dto.ApiResponseDTO;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ApiResponseDTO> resourceNotFoundExceptionHandler(ResourceNotFoundException ex) {
        return new ResponseEntity<>(ApiResponseDTO.builder()
                .message(ex.getMessage())
                .status("Unsuccessful")
                .build(), HttpStatus.NOT_FOUND);
    }
}
