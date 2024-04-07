package com.application.vehicleservicemanagement.service;

import com.application.vehicleservicemanagement.dto.ApiResponse;
import com.application.vehicleservicemanagement.dto.VehicleDto;

import java.util.List;

public interface VehicleService {
    ApiResponse registerVehicle(VehicleDto vehicleDTO);

    VehicleDto getVehicleById(Long vehicleId);

    VehicleDto getVehicleByVehicleNumber(String vehicleNumber);

    List<VehicleDto> getAllVehicles();

    List<VehicleDto> getAllDueVehicles();

    List<VehicleDto> getAllScheduledVehicles();

    List<VehicleDto> getAllVehiclesUnderServicing();

    List<VehicleDto> getAllServicedVehicles();

    ApiResponse scheduleVehicleForService(String vehicleNumber, Long serviceAdvisorId);

    ApiResponse startVehicleService(String vehicleNumber);

    ApiResponse completeVehicleService(String vehicleNumber, List<String> itemNameList);
}
