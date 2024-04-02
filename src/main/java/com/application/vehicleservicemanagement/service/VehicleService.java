package com.application.vehicleservicemanagement.service;

import com.application.vehicleservicemanagement.dto.ApiResponseDTO;
import com.application.vehicleservicemanagement.dto.VehicleDTO;

import java.util.List;

public interface VehicleService {
    ApiResponseDTO registerVehicle(VehicleDTO vehicleDTO);

    VehicleDTO getVehicleById(Long vehicleId);

    VehicleDTO getVehicleByVehicleNumber(String vehicleNumber);

    List<VehicleDTO> getAllVehicles();

    List<VehicleDTO> getAllDueVehicles();

    List<VehicleDTO> getAllScheduledVehicles();

    List<VehicleDTO> getAllVehiclesUnderServicing();

    List<VehicleDTO> getAllServicedVehicles();

    ApiResponseDTO scheduleVehicleForService(String vehicleNumber, Long serviceAdvisorId);

    ApiResponseDTO startVehicleService(String vehicleNumber);

    ApiResponseDTO completeVehicleService(String vehicleNumber, List<String> itemNameList);
}
