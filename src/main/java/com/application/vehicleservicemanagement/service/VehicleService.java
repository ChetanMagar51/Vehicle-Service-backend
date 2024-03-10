package com.application.vehicleservicemanagement.service;

import com.application.vehicleservicemanagement.dto.ApiResponseDTO;
import com.application.vehicleservicemanagement.dto.VehicleDTO;

public interface VehicleService {
    ApiResponseDTO registerVehicle(VehicleDTO vehicleDTO);

    VehicleDTO getVehicleById(Long vehicleId);

    VehicleDTO getVehicleByVehicleNumber(String vehicleNumber);

    ApiResponseDTO scheduleVehicleForService(String vehicleNumber, Long serviceAdvisorId);

    ApiResponseDTO startVehicleService(String vehicleNumber);

    ApiResponseDTO completeVehicleService(String vehicleNumber);
}
