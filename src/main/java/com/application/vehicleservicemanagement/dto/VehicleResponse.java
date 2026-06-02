package com.application.vehicleservicemanagement.dto;

import java.time.LocalDate;
import java.time.LocalTime;

import com.application.vehicleservicemanagement.entity.AdvisorSchedule;
import com.application.vehicleservicemanagement.entity.Owner;
import com.application.vehicleservicemanagement.entity.ServiceStatus;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class VehicleResponse {
    private Long id;
    private String vehicleNumber;
    private String vehicleModel;
    private String vehicleDescription;
    private Owner owner;
    private ServiceStatus serviceStatus;
    
    
  
    private LocalDate scheduleDate;
    private LocalTime startTime;
    private LocalTime endTime;
    
    private AdvisorSchedule advisorSchedule;



    private ServiceAdvisorDto serviceAdvisor;
}
