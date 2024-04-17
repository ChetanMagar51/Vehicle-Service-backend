package com.application.vehicleservicemanagement.service.implementation;

import com.application.vehicleservicemanagement.dto.ApiResponse;
import com.application.vehicleservicemanagement.dto.VehicleDto;
import com.application.vehicleservicemanagement.dto.VehicleResponse;
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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
    public VehicleResponse getVehicleById(Long vehicleId) {
        Vehicle vehicle = vehicleRepository.findById(vehicleId).orElseThrow(() -> new ResourceNotFoundException("Vehicle", "vehicleId", vehicleId.toString()));
        return modelMapper.map(vehicle, VehicleResponse.class);
    }

    @Override
    public VehicleResponse getVehicleByVehicleNumber(String vehicleNumber) {
        Vehicle vehicle = vehicleRepository.findByVehicleNumberIgnoreCase(vehicleNumber).orElseThrow(() -> new ResourceNotFoundException("Vehicle", "vehicleNumber", vehicleNumber));
        return modelMapper.map(vehicle, VehicleResponse.class);
    }

    @Override
    public List<VehicleResponse> getAllVehicles() {
        List<Vehicle> vehicles = vehicleRepository.findAll();
        return vehicles.stream().map(vehicle -> modelMapper.map(vehicle, VehicleResponse.class)).toList();
    }

    @Override
    public List<VehicleResponse> getAllDueVehicles() {
        List<Vehicle> vehicles = vehicleRepository.findAllByServiceStatus(ServiceStatus.DUE);
        return vehicles.stream().map(vehicle -> modelMapper.map(vehicle, VehicleResponse.class)).toList();
    }

    @Override
    public List<VehicleResponse> getAllScheduledVehicles() {
        List<Vehicle> vehicles = vehicleRepository.findAllByServiceStatus(ServiceStatus.SCHEDULED);
        return vehicles.stream().map(vehicle -> modelMapper.map(vehicle, VehicleResponse.class)).toList();
    }

    @Override
    public List<VehicleResponse> getAllVehiclesUnderServicing() {
        List<Vehicle> vehicles = vehicleRepository.findAllByServiceStatus(ServiceStatus.UNDER_SERVICING);
        return vehicles.stream().map(vehicle -> modelMapper.map(vehicle, VehicleResponse.class)).toList();
    }

    @Override
    public List<VehicleResponse> getAllServicedVehicles() {
        return vehicleRepository.findAllByServiceStatus(ServiceStatus.SERVICED).stream().map(vehicle -> modelMapper.map(vehicle, VehicleResponse.class)).toList();
    }

    @Override
    public List<VehicleResponse> getAllVehiclesByServiceAdvisor(Long serviceAdvisorId) {
        User serviceAdvisor = userRepository.findById(serviceAdvisorId).orElseThrow(() -> new ResourceNotFoundException("User", "id", serviceAdvisorId.toString()));
        List<Vehicle> vehicles = serviceAdvisor.getVehicleList();
        return vehicles.stream().map(vehicle -> modelMapper.map(vehicle, VehicleResponse.class)).toList();
    }

    @Override
    public List<VehicleResponse> getScheduledVehiclesByServiceAdvisor(Long serviceAdvisorId) {
        User serviceAdvisor = userRepository.findById(serviceAdvisorId).orElseThrow(() -> new ResourceNotFoundException("User", "id", serviceAdvisorId.toString()));
        List<Vehicle> vehicles = serviceAdvisor.getVehicleList().stream().filter(vehicle -> vehicle.getServiceStatus() == ServiceStatus.SCHEDULED).toList();
        return vehicles.stream().map(vehicle -> modelMapper.map(vehicle, VehicleResponse.class)).toList();
    }

    @Override
    public List<VehicleResponse> getVehiclesUnderServicingByServiceAdvisor(Long serviceAdvisorId) {
        User serviceAdvisor = userRepository.findById(serviceAdvisorId).orElseThrow(() -> new ResourceNotFoundException("User", "id", serviceAdvisorId.toString()));
        List<Vehicle> vehicles = serviceAdvisor.getVehicleList().stream().filter(vehicle -> vehicle.getServiceStatus() == ServiceStatus.UNDER_SERVICING).toList();
        return vehicles.stream().map(vehicle -> modelMapper.map(vehicle, VehicleResponse.class)).toList();
    }

    @Override
    public List<VehicleResponse> getServicedVehiclesByServiceAdvisor(Long serviceAdvisorId) {
        User serviceAdvisor = userRepository.findById(serviceAdvisorId).orElseThrow(() -> new ResourceNotFoundException("User", "id", serviceAdvisorId.toString()));
        List<Vehicle> vehicles = serviceAdvisor.getVehicleList().stream().filter(vehicle -> vehicle.getServiceStatus() == ServiceStatus.SERVICED).toList();
        return vehicles.stream().map(vehicle -> modelMapper.map(vehicle, VehicleResponse.class)).toList();
    }

    @Override
    public ApiResponse scheduleVehicleForService(String vehicleNumber, Long serviceAdvisorId) {
        Vehicle vehicle = vehicleRepository.findByVehicleNumberIgnoreCase(vehicleNumber).orElseThrow(() -> new ResourceNotFoundException("Vehicle", "vehicleNumber", vehicleNumber));
        User serviceAdvisor = userRepository.findByRoleAndId(Role.SERVICE_ADVISOR, serviceAdvisorId).orElseThrow(() -> new ResourceNotFoundException("Service Advisor", "id", serviceAdvisorId.toString()));
        if (!(vehicle.getServiceStatus() == ServiceStatus.DUE) || !advisorService.updateAdvisorStatusDuringScheduling(serviceAdvisor)) {
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
    public ApiResponse completeVehicleService(String vehicleNumber, HashMap<Long, Integer> itemQuantityMap) {
        Vehicle vehicle = vehicleRepository.findByVehicleNumberIgnoreCase(vehicleNumber).orElseThrow(() -> new ResourceNotFoundException("Vehicle", "vehicleNumber", vehicleNumber));
        if (!(vehicle.getServiceStatus() == ServiceStatus.UNDER_SERVICING)) {
            return ApiResponse.builder().message("Failed to process the request!! Try again.").status("Failed").build();
        }
        HashMap<Item, Integer> map = new HashMap<>();
        double amount = 0d;
        for (Map.Entry<Long, Integer> entry : itemQuantityMap.entrySet()) {
            Item item = itemRepository.findById(entry.getKey()).orElseThrow(() -> new ResourceNotFoundException("Item", "id", entry.getKey().toString()));
            map.put(item, entry.getValue());
            amount += item.getPrice() * map.get(item);
        }
        ServiceRecord serviceRecord = ServiceRecord.builder()
                .vehicle(vehicle)
                .serviceAdvisor(vehicle.getServiceAdvisor())
                .itemQuantityMap(map)
                .amount(amount)
                .date(LocalDateTime.now().plusHours(6))
                .isAdminApproved(Boolean.FALSE)
                .isPaymentCompleted(Boolean.FALSE)
                .build();
        vehicle.setServiceRecord(serviceRecord);
        vehicle.setServiceStatus(ServiceStatus.SERVICED);
        vehicleRepository.save(vehicle);
        advisorService.updateAdvisorStatusAfterService(vehicle.getServiceAdvisor(), vehicle);
        return ApiResponse.builder().message("Vehicle service completed.").status("Success").build();
    }

    @Override
    public HashMap<String, Integer> getStatusSummary() {
        HashMap<String, Integer> map = new HashMap<>();
        map.put("All", vehicleRepository.findAll().size());
        map.put("Due", getAllDueVehicles().size());
        map.put("Scheduled", getAllScheduledVehicles().size());
        map.put("Under-servicing", getAllVehiclesUnderServicing().size());
        map.put("Serviced", getAllServicedVehicles().size());
        return map;
    }

    @Override
    public HashMap<String, Integer> getStatusSummaryForAdvisor(Long advisorId) {
        HashMap<String, Integer> map = new HashMap<>();
        map.put("All", getAllVehiclesByServiceAdvisor(advisorId).size());
        map.put("Scheduled", getScheduledVehiclesByServiceAdvisor(advisorId).size());
        map.put("Under-servicing", getVehiclesUnderServicingByServiceAdvisor(advisorId).size());
        map.put("Serviced", getServicedVehiclesByServiceAdvisor(advisorId).size());
        return map;
    }

    private LocalDateTime getExpectedDeliveryTime(Integer queueNumber) {
        LocalDateTime localDateTime = LocalDateTime.now();
        return localDateTime.plusHours(6 + (queueNumber * 2));
    }
}
