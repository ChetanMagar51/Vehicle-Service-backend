package com.application.vehicleservicemanagement.entity;

public enum ScheduleStatus {

    AVAILABLE,      // Free slot

    BOOKED,         // Vehicle assigned

    IN_PROGRESS,    // Service started

    COMPLETED,      // Service finished

    DELAYED,        // Service exceeded estimated time

    CANCELLED ,      // Appointment cancelled
    
    
    ON_LEAVE
}