package com.application.vehicleservicemanagement.service;

import com.application.vehicleservicemanagement.dto.ApiResponse;
import com.application.vehicleservicemanagement.dto.VehicleDto;
import com.application.vehicleservicemanagement.dto.VehicleResponse;

import java.util.HashMap;
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

    List<VehicleResponse> getAllVehiclesByServiceAdvisor(Long serviceAdvisorId);

    List<VehicleResponse> getScheduledVehiclesByServiceAdvisor(Long serviceAdvisorId);

    List<VehicleResponse> getVehiclesUnderServicingByServiceAdvisor(Long serviceAdvisorId);

    List<VehicleResponse> getServicedVehiclesByServiceAdvisor(Long serviceAdvisorId);

    ApiResponse scheduleVehicleForService(String vehicleNumber, Long serviceAdvisorId);

    ApiResponse startVehicleService(String vehicleNumber);

    ApiResponse completeVehicleService(String vehicleNumber, HashMap<Long, Integer> itemQuantityMap);

    HashMap<String, Integer> getStatusSummary();

    HashMap<String, Integer> getStatusSummaryForAdvisor(Long advisorId);
}
