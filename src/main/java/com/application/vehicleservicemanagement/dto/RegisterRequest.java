package com.application.vehicleservicemanagement.dto;

import java.time.LocalTime;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RegisterRequest {
    private String firstName;
    private String lastName;
    private String phone;
    private String address;
    private String email;
    private String password;
    
 // Advisor Availability
    private LocalTime workStartTime;
    private LocalTime workEndTime;
    private Integer slotDuration;

}
