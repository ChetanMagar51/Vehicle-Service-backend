package com.application.vehicleservicemanagement.service;

import com.application.vehicleservicemanagement.dto.ApiResponse;
import com.application.vehicleservicemanagement.dto.VehicleDto;
import com.application.vehicleservicemanagement.entity.Vehicle;

import java.util.List;

public interface VehicleService {
    ApiResponse registerVehicle(VehicleDto vehicleDTO);

    Vehicle getVehicleById(Long vehicleId);

    Vehicle getVehicleByVehicleNumber(String vehicleNumber);

    List<Vehicle> getAllVehicles();

    List<Vehicle> getAllDueVehicles();

    List<Vehicle> getAllScheduledVehicles();

    List<Vehicle> getAllVehiclesUnderServicing();

    List<Vehicle> getAllServicedVehicles();

    ApiResponse scheduleVehicleForService(String vehicleNumber, Long serviceAdvisorId);

    ApiResponse startVehicleService(String vehicleNumber);

    ApiResponse completeVehicleService(String vehicleNumber, List<String> itemNameList);
}
