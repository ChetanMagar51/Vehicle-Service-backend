package com.application.vehicleservicemanagement.exception;

public class InvalidRoleException extends RuntimeException {
    public InvalidRoleException(String role) {
        super(String.format("Invalid role: %s", role));
    }
}
