package com.application.vehicleservicemanagement.service.implementation;

import com.application.vehicleservicemanagement.dto.ApiResponse;
import com.application.vehicleservicemanagement.dto.VehicleDto;
import com.application.vehicleservicemanagement.entity.*;
import com.application.vehicleservicemanagement.exception.ResourceNotFoundException;
import com.application.vehicleservicemanagement.repository.ItemRepository;
import com.application.vehicleservicemanagement.repository.OwnerRepository;
import com.application.vehicleservicemanagement.repository.UserRepository;
import com.application.vehicleservicemanagement.repository.VehicleRepository;
import com.application.vehicleservicemanagement.service.AdvisorService;
import com.application.vehicleservicemanagement.service.VehicleService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class VehicleServiceImplementation implements VehicleService {
    private final VehicleRepository vehicleRepository;
    private final OwnerRepository ownerRepository;
    private final UserRepository userRepository;
    private final ItemRepository itemRepository;
    private final AdvisorService advisorService;
    private final ModelMapper modelMapper;

    @Override
    public ApiResponse registerVehicle(VehicleDto vehicleDto) {
        Owner owner = ownerRepository.findById(vehicleDto.getOwnerId()).orElseThrow(() -> new ResourceNotFoundException("Owner", "id", vehicleDto.getOwnerId().toString()));
        Vehicle vehicle = Vehicle.builder()
                .vehicleNumber(vehicleDto.getVehicleNumber())
                .vehicleModel(vehicleDto.getVehicleModel())
                .vehicleDescription(vehicleDto.getVehicleDescription())
                .owner(owner)
                .serviceStatus(ServiceStatus.DUE)
                .build();
        vehicleRepository.save(vehicle);
        return ApiResponse.builder().message("Vehicle registered successfully.").status("Success").build();
    }

    @Override
    public VehicleDto getVehicleById(Long vehicleId) {
        Vehicle vehicle = vehicleRepository.findById(vehicleId).orElseThrow(() -> new ResourceNotFoundException("Vehicle", "vehicleId", vehicleId.toString()));
        return modelMapper.map(vehicle, VehicleDto.class);
    }

    @Override
    public VehicleDto getVehicleByVehicleNumber(String vehicleNumber) {
        Vehicle vehicle = vehicleRepository.findByVehicleNumberIgnoreCase(vehicleNumber).orElseThrow(() -> new ResourceNotFoundException("Vehicle", "vehicleNumber", vehicleNumber));
        return modelMapper.map(vehicle, VehicleDto.class);
    }

    @Override
    public List<VehicleDto> getAllVehicles() {
        List<Vehicle> vehicleList = vehicleRepository.findAll();
        return vehicleList.stream().map(vehicle -> modelMapper.map(vehicle, VehicleDto.class)).toList();
    }

    @Override
    public List<VehicleDto> getAllDueVehicles() {
        List<Vehicle> vehicleList = vehicleRepository.findAllByServiceStatus(ServiceStatus.DUE);
        return vehicleList.stream().map(vehicle -> modelMapper.map(vehicle, VehicleDto.class)).toList();
    }

    @Override
    public List<VehicleDto> getAllScheduledVehicles() {
        List<Vehicle> vehicleList = vehicleRepository.findAllByServiceStatus(ServiceStatus.SCHEDULED);
        return vehicleList.stream().map(vehicle -> modelMapper.map(vehicle, VehicleDto.class)).toList();
    }

    @Override
    public List<VehicleDto> getAllVehiclesUnderServicing() {
        List<Vehicle> vehicleList = vehicleRepository.findAllByServiceStatus(ServiceStatus.UNDER_SERVICING);
        return vehicleList.stream().map(vehicle -> modelMapper.map(vehicle, VehicleDto.class)).toList();
    }

    @Override
    public List<VehicleDto> getAllServicedVehicles() {
        List<Vehicle> vehicleList = vehicleRepository.findAllByServiceStatus(ServiceStatus.SERVICED);
        return vehicleList.stream().map(vehicle -> modelMapper.map(vehicle, VehicleDto.class)).toList();
    }

    @Override
    public ApiResponse scheduleVehicleForService(String vehicleNumber, Long serviceAdvisorId) {
        Vehicle vehicle = vehicleRepository.findByVehicleNumberIgnoreCase(vehicleNumber).orElseThrow(() -> new ResourceNotFoundException("Vehicle", "vehicleNumber", vehicleNumber));
        User serviceAdvisor = userRepository.findByRoleAndId(Role.SERVICE_ADVISOR, serviceAdvisorId).orElseThrow(() -> new ResourceNotFoundException("Service Advisor", "id", serviceAdvisorId.toString()));
        if (!(vehicle.getServiceStatus() == ServiceStatus.DUE) || !advisorService.updateAdvisorStatus(serviceAdvisor)) {
            return ApiResponse.builder().message("Failed to process the request!! Try again.").status("Failed").build();
        }
        vehicle.setServiceAdvisor(serviceAdvisor);
        vehicle.setServiceStatus(ServiceStatus.SCHEDULED);
        vehicle.setQueueNumber(serviceAdvisor.getScheduledVehicleCount());
        vehicle.setDeliveryTime(getExpectedDeliveryTime(vehicle.getQueueNumber()));
        vehicleRepository.save(vehicle);
        return ApiResponse.builder().message("Vehicle scheduled successfully.").status("Success").build();
    }

    @Override
    public ApiResponse startVehicleService(String vehicleNumber) {
        Vehicle vehicle = vehicleRepository.findByVehicleNumberIgnoreCase(vehicleNumber).orElseThrow(() -> new ResourceNotFoundException("Vehicle", "vehicleNumber", vehicleNumber));
        if (!(vehicle.getServiceStatus() == ServiceStatus.SCHEDULED)) {
            return ApiResponse.builder().message("Failed to process the request!! Try again.").status("Failed").build();
        }
        vehicle.setServiceStatus(ServiceStatus.UNDER_SERVICING);
        vehicleRepository.save(vehicle);
        return ApiResponse.builder().message("Service started for vehicle.").status("Success").build();
    }

    @Override
    public ApiResponse completeVehicleService(String vehicleNumber, List<String> itemNameList) {
        Vehicle vehicle = vehicleRepository.findByVehicleNumberIgnoreCase(vehicleNumber).orElseThrow(() -> new ResourceNotFoundException("Vehicle", "vehicleNumber", vehicleNumber));
        if (!(vehicle.getServiceStatus() == ServiceStatus.UNDER_SERVICING)) {
            return ApiResponse.builder().message("Failed to process the request!! Try again.").status("Failed").build();
        }
        List<Optional<Item>> itemList = itemNameList.stream().map(itemRepository::findByNameIgnoreCase).toList();
        ServiceRecord serviceRecord = ServiceRecord.builder()
                .vehicle(vehicle)
                .serviceAdvisor(vehicle.getServiceAdvisor())
                .itemList(itemList.stream().map(item -> modelMapper.map(item, Item.class)).toList())
                .amount(itemList.stream().mapToDouble(item -> item.get().getPrice()).sum())
                .date(LocalDateTime.now().plusHours(6))
                .isAdminApproved(false)
                .isPaymentCompleted(false)
                .build();
        vehicle.setServiceRecord(serviceRecord);
        vehicle.setServiceStatus(ServiceStatus.SERVICED);
        vehicleRepository.save(vehicle);
        return ApiResponse.builder().message("Vehicle service completed.").status("Success").build();
    }

    private LocalDateTime getExpectedDeliveryTime(Integer queueNumber) {
        LocalDateTime localDateTime = LocalDateTime.now();
        return localDateTime.plusHours(6 + (queueNumber * 2));
    }
}
