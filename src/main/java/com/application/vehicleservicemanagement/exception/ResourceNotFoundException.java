package com.application.vehicleservicemanagement.exception;

public class ResourceNotFoundException extends RuntimeException {
    public ResourceNotFoundException(String resourceName, String attributeName, String attributeValue) {
        super(String.format("%s not found with %s: %s", resourceName, attributeName, attributeValue));
    }
}
