package com.application.vehicleservicemanagement.service.implementation;

import com.application.vehicleservicemanagement.dto.ApiResponseDTO;
import com.application.vehicleservicemanagement.dto.VehicleDTO;
import com.application.vehicleservicemanagement.entity.ServiceAdvisor;
import com.application.vehicleservicemanagement.entity.ServiceStatus;
import com.application.vehicleservicemanagement.entity.Vehicle;
import com.application.vehicleservicemanagement.exception.ResourceNotFoundException;
import com.application.vehicleservicemanagement.repository.UserRepository;
import com.application.vehicleservicemanagement.repository.VehicleRepository;
import com.application.vehicleservicemanagement.service.VehicleService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class VehicleServiceImplementation implements VehicleService {
    private final VehicleRepository vehicleRepository;
    private final UserRepository userRepository;
    private final ModelMapper modelMapper;

    @Override
    public ApiResponseDTO registerVehicle(VehicleDTO vehicleDTO) {
        Vehicle vehicle = Vehicle.builder()
                .vehicleNumber(vehicleDTO.getVehicleNumber())
                .vehicleModel(vehicleDTO.getVehicleModel())
                .vehicleDescription(vehicleDTO.getVehicleDescription())
                .serviceStatus(ServiceStatus.DUE)
                .build();
        vehicleRepository.save(vehicle);
        return ApiResponseDTO.builder().message("Vehicle registered successfully.").status("Success").build();
    }

    @Override
    public VehicleDTO getVehicleById(Long vehicleId) {
        Vehicle vehicle = vehicleRepository.findById(vehicleId).orElseThrow(() -> new ResourceNotFoundException("Vehicle", "vehicleId", vehicleId.toString()));
        return modelMapper.map(vehicle, VehicleDTO.class);
    }

    @Override
    public VehicleDTO getVehicleByVehicleNumber(String vehicleNumber) {
        Vehicle vehicle = vehicleRepository.findByVehicleNumberIgnoreCase(vehicleNumber).orElseThrow(() -> new ResourceNotFoundException("Vehicle", "vehicleId", vehicleNumber));
        return modelMapper.map(vehicle, VehicleDTO.class);
    }

    @Override
    public ApiResponseDTO scheduleVehicleForService(String vehicleNumber, Long serviceAdvisorId) {
        Vehicle vehicle = vehicleRepository.findByVehicleNumberIgnoreCase(vehicleNumber).orElseThrow(() -> new ResourceNotFoundException("Vehicle", "vehicleId", vehicleNumber));
        ServiceAdvisor serviceAdvisor = (ServiceAdvisor) userRepository.findById(serviceAdvisorId).orElseThrow(() -> new ResourceNotFoundException("Service Advisor", "id", serviceAdvisorId.toString()));
        if (!(vehicle.getServiceStatus() == ServiceStatus.DUE)) {
            return ApiResponseDTO.builder().status("Failed to process the request!! Try again.").status("Failed").build();
        }
        vehicle.setServiceAdvisor(serviceAdvisor);
        vehicle.setServiceStatus(ServiceStatus.SCHEDULED);
        vehicleRepository.save(vehicle);
        return ApiResponseDTO.builder().message("Vehicle scheduled successfully.").status("Success").build();
    }

    @Override
    public ApiResponseDTO startVehicleService(String vehicleNumber) {
        Vehicle vehicle = vehicleRepository.findByVehicleNumberIgnoreCase(vehicleNumber).orElseThrow(() -> new ResourceNotFoundException("Vehicle", "vehicleId", vehicleNumber));
        if (!(vehicle.getServiceStatus() == ServiceStatus.SCHEDULED)) {
            return ApiResponseDTO.builder().status("Failed to process the request!! Try again.").status("Failed").build();
        }
        vehicle.setServiceStatus(ServiceStatus.UNDER_SERVICING);
        vehicleRepository.save(vehicle);
        return ApiResponseDTO.builder().message("Service started for vehicle.").status("Success").build();
    }

    @Override
    public ApiResponseDTO completeVehicleService(String vehicleNumber) {
        Vehicle vehicle = vehicleRepository.findByVehicleNumberIgnoreCase(vehicleNumber).orElseThrow(() -> new ResourceNotFoundException("Vehicle", "vehicleId", vehicleNumber));
        if (!(vehicle.getServiceStatus() == ServiceStatus.UNDER_SERVICING)) {
            return ApiResponseDTO.builder().status("Failed to process the request!! Try again.").status("Failed").build();
        }
        vehicle.setServiceStatus(ServiceStatus.SERVICED);
        vehicleRepository.save(vehicle);
        return ApiResponseDTO.builder().message("Vehicle service completed.").status("Success").build();
    }
}
