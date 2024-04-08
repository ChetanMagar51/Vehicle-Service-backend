package com.application.vehicleservicemanagement.service;

import com.application.vehicleservicemanagement.dto.ApiResponse;
import com.application.vehicleservicemanagement.dto.VehicleDto;
import com.application.vehicleservicemanagement.dto.VehicleResponse;
import com.application.vehicleservicemanagement.entity.Vehicle;

import java.util.List;

public interface VehicleService {
    ApiResponse registerVehicle(VehicleDto vehicleDTO);

    VehicleResponse getVehicleById(Long vehicleId);

    VehicleResponse getVehicleByVehicleNumber(String vehicleNumber);

    List<VehicleResponse> getAllVehicles();

    List<VehicleResponse> getAllDueVehicles();

    List<VehicleResponse> getAllScheduledVehicles();

    List<VehicleResponse> getAllVehiclesUnderServicing();

    List<VehicleResponse> getAllServicedVehicles();

    ApiResponse scheduleVehicleForService(String vehicleNumber, Long serviceAdvisorId);

    ApiResponse startVehicleService(String vehicleNumber);

    ApiResponse completeVehicleService(String vehicleNumber, List<String> itemNameList);
}
