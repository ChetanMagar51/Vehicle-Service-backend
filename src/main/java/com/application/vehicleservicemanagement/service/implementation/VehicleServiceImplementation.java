package com.application.vehicleservicemanagement.service.implementation;

import com.application.vehicleservicemanagement.dto.ApiResponseDTO;
import com.application.vehicleservicemanagement.dto.VehicleDTO;
import com.application.vehicleservicemanagement.entity.*;
import com.application.vehicleservicemanagement.exception.ResourceNotFoundException;
import com.application.vehicleservicemanagement.repository.ItemRepository;
import com.application.vehicleservicemanagement.repository.UserRepository;
import com.application.vehicleservicemanagement.repository.VehicleRepository;
import com.application.vehicleservicemanagement.service.VehicleService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class VehicleServiceImplementation implements VehicleService {
    private final VehicleRepository vehicleRepository;
    private final UserRepository userRepository;
    private final ItemRepository itemRepository;
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
        Vehicle vehicle = vehicleRepository.findByVehicleNumberIgnoreCase(vehicleNumber).orElseThrow(() -> new ResourceNotFoundException("Vehicle", "vehicleNumber", vehicleNumber));
        return modelMapper.map(vehicle, VehicleDTO.class);
    }

    @Override
    public List<VehicleDTO> getAllVehicles() {
        List<Vehicle> vehicleList = vehicleRepository.findAll();
        return vehicleList.stream().map(vehicle -> modelMapper.map(vehicle, VehicleDTO.class)).toList();
    }

    @Override
    public List<VehicleDTO> getAllDueVehicles() {
        List<Vehicle> vehicleList = vehicleRepository.findAllByServiceStatus(ServiceStatus.DUE);
        return vehicleList.stream().map(vehicle -> modelMapper.map(vehicle, VehicleDTO.class)).toList();
    }

    @Override
    public List<VehicleDTO> getAllScheduledVehicles() {
        List<Vehicle> vehicleList = vehicleRepository.findAllByServiceStatus(ServiceStatus.SCHEDULED);
        return vehicleList.stream().map(vehicle -> modelMapper.map(vehicle, VehicleDTO.class)).toList();
    }

    @Override
    public List<VehicleDTO> getAllVehiclesUnderServicing() {
        List<Vehicle> vehicleList = vehicleRepository.findAllByServiceStatus(ServiceStatus.UNDER_SERVICING);
        return vehicleList.stream().map(vehicle -> modelMapper.map(vehicle, VehicleDTO.class)).toList();
    }

    @Override
    public List<VehicleDTO> getAllServicedVehicles() {
        List<Vehicle> vehicleList = vehicleRepository.findAllByServiceStatus(ServiceStatus.SERVICED);
        return vehicleList.stream().map(vehicle -> modelMapper.map(vehicle, VehicleDTO.class)).toList();
    }

    @Override
    public ApiResponseDTO scheduleVehicleForService(String vehicleNumber, Long serviceAdvisorId) {
        Vehicle vehicle = vehicleRepository.findByVehicleNumberIgnoreCase(vehicleNumber).orElseThrow(() -> new ResourceNotFoundException("Vehicle", "vehicleNumber", vehicleNumber));
        User user = userRepository.findByRoleAndId(Role.SERVICE_ADVISOR, serviceAdvisorId).orElseThrow(() -> new ResourceNotFoundException("Service Advisor", "id", serviceAdvisorId.toString()));
        if (!(vehicle.getServiceStatus() == ServiceStatus.DUE)) {
            return ApiResponseDTO.builder().message("Failed to process the request!! Try again.").status("Failed").build();
        }
        vehicle.setServiceAdvisor(user);
        vehicle.setServiceStatus(ServiceStatus.SCHEDULED);
        vehicleRepository.save(vehicle);
        return ApiResponseDTO.builder().message("Vehicle scheduled successfully.").status("Success").build();
    }

    @Override
    public ApiResponseDTO startVehicleService(String vehicleNumber) {
        Vehicle vehicle = vehicleRepository.findByVehicleNumberIgnoreCase(vehicleNumber).orElseThrow(() -> new ResourceNotFoundException("Vehicle", "vehicleNumber", vehicleNumber));
        if (!(vehicle.getServiceStatus() == ServiceStatus.SCHEDULED)) {
            return ApiResponseDTO.builder().message("Failed to process the request!! Try again.").status("Failed").build();
        }
        vehicle.setServiceStatus(ServiceStatus.UNDER_SERVICING);
        vehicleRepository.save(vehicle);
        return ApiResponseDTO.builder().message("Service started for vehicle.").status("Success").build();
    }

    @Override
    public ApiResponseDTO completeVehicleService(String vehicleNumber, List<String> itemNameList) {
        Vehicle vehicle = vehicleRepository.findByVehicleNumberIgnoreCase(vehicleNumber).orElseThrow(() -> new ResourceNotFoundException("Vehicle", "vehicleNumber", vehicleNumber));
        if (!(vehicle.getServiceStatus() == ServiceStatus.UNDER_SERVICING)) {
            return ApiResponseDTO.builder().message("Failed to process the request!! Try again.").status("Failed").build();
        }
        List<Optional<Item>> itemList = itemNameList.stream().map(itemRepository::findByNameIgnoreCase).toList();
        ServiceRecord serviceRecord = ServiceRecord.builder()
                .vehicle(vehicle)
                .serviceAdvisor(vehicle.getServiceAdvisor())
                .itemList(itemList.stream().map(item -> modelMapper.map(item, Item.class)).toList())
                .amount(itemList.stream().mapToDouble(item -> item.get().getPrice()).sum())
                .date(new Date())
                .isAdminApproved(false)
                .isPaymentCompleted(false)
                .build();
        vehicle.setServiceRecord(serviceRecord);
        vehicle.setServiceStatus(ServiceStatus.SERVICED);
        vehicleRepository.save(vehicle);
        return ApiResponseDTO.builder().message("Vehicle service completed.").status("Success").build();
    }
}
