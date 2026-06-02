package com.application.vehicleservicemanagement.service.implementation;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.application.vehicleservicemanagement.dto.AdvisorAvailabilityDto;
import com.application.vehicleservicemanagement.dto.AdvisorScheduleDto;
import com.application.vehicleservicemanagement.dto.ApiResponse;
import com.application.vehicleservicemanagement.dto.ServiceAdvisorDto;
import com.application.vehicleservicemanagement.dto.UserDto;
import com.application.vehicleservicemanagement.dto.VehicleDto;
import com.application.vehicleservicemanagement.dto.VehicleResponse;
import com.application.vehicleservicemanagement.entity.AdvisorSchedule;
import com.application.vehicleservicemanagement.entity.Item;
import com.application.vehicleservicemanagement.entity.Owner;
import com.application.vehicleservicemanagement.entity.Role;
import com.application.vehicleservicemanagement.entity.ScheduleStatus;
import com.application.vehicleservicemanagement.entity.ServiceRecord;
import com.application.vehicleservicemanagement.entity.ServiceStatus;
import com.application.vehicleservicemanagement.entity.User;
import com.application.vehicleservicemanagement.entity.Vehicle;
import com.application.vehicleservicemanagement.exception.ResourceNotFoundException;
import com.application.vehicleservicemanagement.repository.AdvisorScheduleRepository;
import com.application.vehicleservicemanagement.repository.ItemRepository;
import com.application.vehicleservicemanagement.repository.OwnerRepository;
import com.application.vehicleservicemanagement.repository.UserRepository;
import com.application.vehicleservicemanagement.repository.VehicleRepository;
import com.application.vehicleservicemanagement.service.AdvisorService;
import com.application.vehicleservicemanagement.service.StockService;
import com.application.vehicleservicemanagement.service.VehicleService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class VehicleServiceImplementation implements VehicleService {
    private final VehicleRepository vehicleRepository;
    private final OwnerRepository ownerRepository;
    private final UserRepository userRepository;
    private final ItemRepository itemRepository;
    private final AdvisorService advisorService;
    private final EmailService emailService;
    private final ModelMapper modelMapper;
    
    
    private final StockService stockService;
    
    private final AdvisorScheduleRepository advisorScheduleRepository;

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

//    @Override
//    public List<VehicleResponse> getAllScheduledVehicles() {
//        List<Vehicle> vehicles = vehicleRepository.findAllByServiceStatus(ServiceStatus.SCHEDULED);
//        
//        return vehicles.stream().map(vehicle -> modelMapper.map(vehicle, VehicleResponse.class)).toList();
//    }
    
    
    @Override
    public List<VehicleResponse> getAllScheduledVehicles() {

        return vehicleRepository.findAllByServiceStatus(ServiceStatus.SCHEDULED)
                .stream()
                .map(vehicle -> {

                    VehicleResponse response = new VehicleResponse();

                    response.setId(vehicle.getId());
                    response.setVehicleNumber(vehicle.getVehicleNumber());
                    response.setVehicleModel(vehicle.getVehicleModel());
                    response.setVehicleDescription(vehicle.getVehicleDescription());
                    response.setServiceStatus(vehicle.getServiceStatus());

                    // Owner
                    if (vehicle.getOwner() != null) {
                        response.setOwner(
                                
                                        vehicle.getOwner());
                    }
                                      

                    // Schedule Details
                    AdvisorSchedule schedule =
                            advisorScheduleRepository
                                    .findByVehicle(vehicle)
                                    .orElse(null);

                    if (schedule != null) {

                        response.setScheduleDate(
                                schedule.getScheduleDate());

                        response.setStartTime(
                                schedule.getStartTime());

                        response.setEndTime(
                                schedule.getEndTime());

                        User advisorUser =
                                schedule.getServiceAdvisor();

                        if (advisorUser != null) {

                            UserDto advisorUserDto =
                                    modelMapper.map(
                                            advisorUser,
                                            UserDto.class);

                            List<AdvisorScheduleDto> schedules = List.of(
                                    modelMapper.map(
                                            schedule,
                                            AdvisorScheduleDto.class)
                            );

                            ServiceAdvisorDto advisorDto =
                                    ServiceAdvisorDto.builder()
                                            .advisor(advisorUserDto)
                                            .schedules(schedules)
                                            .build();

                            response.setServiceAdvisor(advisorDto);
                        }
                    }

                    return response;

                })
                .collect(Collectors.toList());
    }
    

//    @Override
//    public List<VehicleResponse> getAllVehiclesUnderServicing() {
//        List<Vehicle> vehicles = vehicleRepository.findAllByServiceStatus(ServiceStatus.UNDER_SERVICING);
//        return vehicles.stream().map(vehicle -> modelMapper.map(vehicle, VehicleResponse.class)).toList();
//    }
    
    
    @Override
    public List<VehicleResponse> getAllVehiclesUnderServicing() {

        return vehicleRepository
                .findAllByServiceStatus(ServiceStatus.UNDER_SERVICING)
                .stream()
                .map(vehicle -> {

                    VehicleResponse response = VehicleResponse.builder()
                            .id(vehicle.getId())
                            .vehicleNumber(vehicle.getVehicleNumber())
                            .vehicleModel(vehicle.getVehicleModel())
                            .vehicleDescription(vehicle.getVehicleDescription())
                            .serviceStatus(vehicle.getServiceStatus())
//                            .deliveryTime(vehicle.getDeliveryTime())
//                            .queueNumber(vehicle.getQueueNumber())
                            .build();

                    // Owner
                    if (vehicle.getOwner() != null) {
                        response.setOwner(vehicle.getOwner());
                    }

                    // Schedule
                    AdvisorSchedule schedule = advisorScheduleRepository
                            .findByVehicle(vehicle)
                            .orElse(null);

                    if (schedule != null) {

                        // ⬇️ PLANNED SCHEDULE TIMES
                        response.setScheduleDate(schedule.getScheduleDate());
                        response.setStartTime(schedule.getStartTime());
                        response.setEndTime(schedule.getEndTime());
                        
                        

                        // ⬇️ ACTUAL SERVICE TIMES (IMPORTANT ADDITION)
//                        response.setActualStartTime(schedule.getActualStartTime());
//                        response.setActualEndTime(schedule.getActualEndTime());
//
//                        // Optional but useful for UI
//                        response.setScheduleStatus(schedule.getStatus());

                        User advisor = schedule.getServiceAdvisor();

                        if (advisor != null) {

                            UserDto advisorDto = UserDto.builder()
                                    .id(advisor.getId())
                                    .firstName(advisor.getFirstName())
                                    .lastName(advisor.getLastName())
                                    .phone(advisor.getPhone())
                                    .email(advisor.getEmail())
                                    .address(advisor.getAddress())
                                    .build();

                            AdvisorScheduleDto scheduleDto = AdvisorScheduleDto.builder()
                                    .id(schedule.getId())
                                    .scheduleDate(schedule.getScheduleDate())
                                    .startTime(schedule.getStartTime())
                                    .endTime(schedule.getEndTime())
                                    .status(schedule.getStatus())
                                    .actualStartTime(schedule.getActualStartTime())
                                    .actualEndTime(schedule.getActualEndTime())
                                    
                                    
                                    .build();

                            ServiceAdvisorDto serviceAdvisorDto = ServiceAdvisorDto.builder()
                                    .advisor(advisorDto)
                                    .schedules(List.of(scheduleDto))
                                    .availability(null)
                                    .build();

                            response.setServiceAdvisor(serviceAdvisorDto);
                        }
                    }

                    return response;
                })
                .toList();
    }

//    @Override
//    public List<VehicleResponse> getAllServicedVehicles() {
//        return vehicleRepository.findAllByServiceStatus(ServiceStatus.SERVICED).stream().map(vehicle -> modelMapper.map(vehicle, VehicleResponse.class)).toList();
//    }
    
    @Override
    public List<VehicleResponse> getAllServicedVehicles() {

        return vehicleRepository
                .findAllByServiceStatus(ServiceStatus.SERVICED)
                .stream()
                .map(this::buildVehicleResponse)
                .collect(Collectors.toList());
    }
    
    

//    @Override
//    public List<VehicleResponse> getAllVehiclesByServiceAdvisor(Long serviceAdvisorId) {
//        User serviceAdvisor = userRepository.findById(serviceAdvisorId).orElseThrow(() -> new ResourceNotFoundException("User", "id", serviceAdvisorId.toString()));
//        List<Vehicle> vehicles = serviceAdvisor.getVehicleList();
//        return vehicles.stream().map(vehicle -> modelMapper.map(vehicle, VehicleResponse.class)).toList();
//    }
    
    @Override
    public List<VehicleResponse> getAllVehiclesByServiceAdvisor(Long serviceAdvisorId) {

        User serviceAdvisor = userRepository.findById(serviceAdvisorId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "User",
                        "id",
                        serviceAdvisorId.toString()));

        List<Vehicle> vehicles = serviceAdvisor.getVehicleList();

        return vehicles.stream()
                .map(vehicle -> {

                    VehicleResponse response = new VehicleResponse();

                    response.setId(vehicle.getId());
                    response.setVehicleNumber(vehicle.getVehicleNumber());
                    response.setVehicleModel(vehicle.getVehicleModel());
                    response.setVehicleDescription(vehicle.getVehicleDescription());
                    response.setOwner(vehicle.getOwner());
                    response.setServiceStatus(vehicle.getServiceStatus());

                    AdvisorSchedule schedule = vehicle.getSchedule();

                    if (schedule != null) {

                        response.setScheduleDate(schedule.getScheduleDate());
                        response.setStartTime(schedule.getStartTime());
                        response.setEndTime(schedule.getEndTime());

                        User advisor = schedule.getServiceAdvisor();

                        if (advisor != null) {

                            UserDto advisorUserDto = UserDto.builder()
                                    .id(advisor.getId())
                                    .firstName(advisor.getFirstName())
                                    .lastName(advisor.getLastName())
                                    .phone(advisor.getPhone())
                                    .email(advisor.getEmail())
                                    .address(advisor.getAddress())
                                    .build();

                            AdvisorScheduleDto scheduleDto = AdvisorScheduleDto.builder()
                                    .id(schedule.getId())
                                    .scheduleDate(schedule.getScheduleDate())
                                    .startTime(schedule.getStartTime())
                                    .endTime(schedule.getEndTime())
                                    .status(schedule.getStatus())
                                    .build();

                            ServiceAdvisorDto serviceAdvisorDto = ServiceAdvisorDto.builder()
                                    .advisor(advisorUserDto)
                                    .schedules(List.of(scheduleDto))
                                    .build();

                            response.setServiceAdvisor(serviceAdvisorDto);
                        }
                    }

                    return response;

                })
                .collect(Collectors.toList());
    }

//    @Override
//    public List<VehicleResponse> getScheduledVehiclesByServiceAdvisor(Long serviceAdvisorId) {
//        User serviceAdvisor = userRepository.findById(serviceAdvisorId).orElseThrow(() -> new ResourceNotFoundException("User", "id", serviceAdvisorId.toString()));
//        List<Vehicle> vehicles = serviceAdvisor.getVehicleList().stream().filter(vehicle -> vehicle.getServiceStatus() == ServiceStatus.SCHEDULED).toList();
//        return vehicles.stream().map(vehicle -> modelMapper.map(vehicle, VehicleResponse.class)).toList();
//    }
    
       
    @Override
    public List<VehicleResponse> getScheduledVehiclesByServiceAdvisor(Long serviceAdvisorId) {

        User serviceAdvisor = userRepository.findById(serviceAdvisorId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "User",
                        "id",
                        serviceAdvisorId.toString()));

        List<Vehicle> vehicles = serviceAdvisor.getVehicleList()
                .stream()
                .filter(vehicle -> vehicle.getServiceStatus() == ServiceStatus.SCHEDULED)
                .collect(Collectors.toList());

        return vehicles.stream().map(vehicle -> {

            VehicleResponse response = VehicleResponse.builder()
                    .id(vehicle.getId())
                    .vehicleNumber(vehicle.getVehicleNumber())
                    .vehicleModel(vehicle.getVehicleModel())
                    .vehicleDescription(vehicle.getVehicleDescription())
                    .owner(vehicle.getOwner())
                    .serviceStatus(vehicle.getServiceStatus())
                    
                    .build();
            


            AdvisorSchedule schedule = advisorScheduleRepository
                    .findByVehicle(vehicle)
                    .orElse(null);

            if (schedule != null) {

                response.setScheduleDate(schedule.getScheduleDate());
                response.setStartTime(schedule.getStartTime());
                response.setEndTime(schedule.getEndTime());

                User advisor = schedule.getServiceAdvisor();

                if (advisor != null) {

                    UserDto advisorDto = UserDto.builder()
                            .id(advisor.getId())
                            .firstName(advisor.getFirstName())
                            .lastName(advisor.getLastName())
                            .phone(advisor.getPhone())
                            .email(advisor.getEmail())
                            .address(advisor.getAddress())
                            .build();

                    AdvisorScheduleDto scheduleDto = AdvisorScheduleDto.builder()
                            .id(schedule.getId())
                            .scheduleDate(schedule.getScheduleDate())
                            .startTime(schedule.getStartTime())
                            .endTime(schedule.getEndTime())
                            .status(schedule.getStatus())
                            .build();

                    AdvisorAvailabilityDto availabilityDto = null;

                    

                    ServiceAdvisorDto serviceAdvisorDto = ServiceAdvisorDto.builder()
                            .advisor(advisorDto)
                            .schedules(List.of(scheduleDto))
                            .availability(availabilityDto)
                            .build();

                    response.setServiceAdvisor(serviceAdvisorDto);
                }
            }

            return response;

        }).sorted(
                Comparator.comparing(
                        VehicleResponse::getScheduleDate,
                        Comparator.nullsLast(Comparator.naturalOrder()))
                        .thenComparing(
                                VehicleResponse::getStartTime,
                                Comparator.nullsLast(Comparator.naturalOrder()))
        )
        		.toList();
    }
    
    
//    @Override
//    public List<VehicleResponse> getVehiclesUnderServicingByServiceAdvisor(Long serviceAdvisorId) {
//        User serviceAdvisor = userRepository.findById(serviceAdvisorId).orElseThrow(() -> new ResourceNotFoundException("User", "id", serviceAdvisorId.toString()));
//        List<Vehicle> vehicles = serviceAdvisor.getVehicleList().stream().filter(vehicle -> vehicle.getServiceStatus() == ServiceStatus.UNDER_SERVICING).toList();
//        return vehicles.stream().map(vehicle -> modelMapper.map(vehicle, VehicleResponse.class)).toList();
//    }
    
    @Override
    public List<VehicleResponse> getVehiclesUnderServicingByServiceAdvisor(Long serviceAdvisorId) {

        User serviceAdvisor = userRepository.findById(serviceAdvisorId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "User",
                        "id",
                        serviceAdvisorId.toString()));

        return serviceAdvisor.getVehicleList()
                .stream()
                .filter(vehicle ->
                        vehicle.getServiceStatus() == ServiceStatus.UNDER_SERVICING)
                .map(vehicle -> {

                    VehicleResponse response = VehicleResponse.builder()
                            .id(vehicle.getId())
                            .vehicleNumber(vehicle.getVehicleNumber())
                            .vehicleModel(vehicle.getVehicleModel())
                            .vehicleDescription(vehicle.getVehicleDescription())
                            .owner(vehicle.getOwner())
                            .serviceStatus(vehicle.getServiceStatus())
//                            .deliveryTime(vehicle.getDeliveryTime())
//                            .queueNumber(vehicle.getQueueNumber())
                            .build();

                    AdvisorSchedule schedule = advisorScheduleRepository
                            .findByVehicle(vehicle)
                            .orElse(null);

                    if (schedule != null) {

                        response.setScheduleDate(schedule.getScheduleDate());
                        response.setStartTime(schedule.getStartTime());
                        response.setEndTime(schedule.getEndTime());

                        User advisor = schedule.getServiceAdvisor();

                        if (advisor != null) {

                            UserDto advisorDto = UserDto.builder()
                                    .id(advisor.getId())
                                    .firstName(advisor.getFirstName())
                                    .lastName(advisor.getLastName())
                                    .phone(advisor.getPhone())
                                    .email(advisor.getEmail())
                                    .address(advisor.getAddress())
                                    .build();

                            AdvisorScheduleDto scheduleDto = AdvisorScheduleDto.builder()
                                    .id(schedule.getId())
                                    .scheduleDate(schedule.getScheduleDate())
                                    .startTime(schedule.getStartTime())
                                    .endTime(schedule.getEndTime())
                                    .status(schedule.getStatus())
                                    .actualStartTime(schedule.getActualStartTime())
                                    .actualEndTime(schedule.getActualEndTime())
                                    .build();

                            ServiceAdvisorDto serviceAdvisorDto =
                                    ServiceAdvisorDto.builder()
                                            .advisor(advisorDto)
                                            .schedules(List.of(scheduleDto))
                                            .availability(null)
                                            .build();

                            response.setServiceAdvisor(serviceAdvisorDto);
                        }
                    }

                    return response;
                })
                .toList();
    }

//    @Override
//    public List<VehicleResponse> getServicedVehiclesByServiceAdvisor(Long serviceAdvisorId) {
//        User serviceAdvisor = userRepository.findById(serviceAdvisorId).orElseThrow(() -> new ResourceNotFoundException("User", "id", serviceAdvisorId.toString()));
//        List<Vehicle> vehicles = serviceAdvisor.getVehicleList().stream().filter(vehicle -> vehicle.getServiceStatus() == ServiceStatus.SERVICED).toList();
//        return vehicles.stream().map(vehicle -> modelMapper.map(vehicle, VehicleResponse.class)).toList();
//    }

    @Override
    public List<VehicleResponse> getServicedVehiclesByServiceAdvisor(Long serviceAdvisorId) {

        User serviceAdvisor = userRepository.findById(serviceAdvisorId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "User",
                        "id",
                        serviceAdvisorId.toString()));

        return serviceAdvisor.getVehicleList()
                .stream()
                .filter(vehicle ->
                        vehicle.getServiceStatus() == ServiceStatus.SERVICED)
                .map(vehicle -> {

                    VehicleResponse response = VehicleResponse.builder()
                            .id(vehicle.getId())
                            .vehicleNumber(vehicle.getVehicleNumber())
                            .vehicleModel(vehicle.getVehicleModel())
                            .vehicleDescription(vehicle.getVehicleDescription())
                            .owner(vehicle.getOwner())
                            .serviceStatus(vehicle.getServiceStatus())
//                            .deliveryTime(vehicle.getDeliveryTime())
//                            .queueNumber(vehicle.getQueueNumber())
                            .build();

                    AdvisorSchedule schedule = advisorScheduleRepository
                            .findByVehicle(vehicle)
                            .orElse(null);

                    if (schedule != null) {

                        response.setScheduleDate(schedule.getScheduleDate());
                        response.setStartTime(schedule.getStartTime());
                        response.setEndTime(schedule.getEndTime());

                        User advisor = schedule.getServiceAdvisor();

                        if (advisor != null) {

                            UserDto advisorDto = UserDto.builder()
                                    .id(advisor.getId())
                                    .firstName(advisor.getFirstName())
                                    .lastName(advisor.getLastName())
                                    .phone(advisor.getPhone())
                                    .email(advisor.getEmail())
                                    .address(advisor.getAddress())
                                    .build();

                            AdvisorScheduleDto scheduleDto = AdvisorScheduleDto.builder()
                                    .id(schedule.getId())
                                    .scheduleDate(schedule.getScheduleDate())
                                    .startTime(schedule.getStartTime())
                                    .endTime(schedule.getEndTime())
                                    .status(schedule.getStatus())
                                    .build();

                            ServiceAdvisorDto serviceAdvisorDto =
                                    ServiceAdvisorDto.builder()
                                            .advisor(advisorDto)
                                            .schedules(List.of(scheduleDto))
                                            .availability(null)
                                            .build();

                            response.setServiceAdvisor(serviceAdvisorDto);
                        }
                    }

                    return response;
                })
                .toList();
    }
    
    
    //for vehicle schedule for service
//    @Override
//    public ApiResponse scheduleVehicleForService(String vehicleNumber, Long serviceAdvisorId) {
//        Vehicle vehicle = vehicleRepository.findByVehicleNumberIgnoreCase(vehicleNumber).orElseThrow(() -> new ResourceNotFoundException("Vehicle", "vehicleNumber", vehicleNumber));
//        User serviceAdvisor = userRepository.findByRoleAndId(Role.SERVICE_ADVISOR, serviceAdvisorId).orElseThrow(() -> new ResourceNotFoundException("Service Advisor", "id", serviceAdvisorId.toString()));
//        if (!(vehicle.getServiceStatus() == ServiceStatus.DUE) || !advisorService.updateAdvisorStatusDuringScheduling(serviceAdvisor)) {
//            return ApiResponse.builder().message("Failed to process the request!! Try again.").status("Failed").build();
//        }
//        vehicle.setServiceAdvisor(serviceAdvisor);
//        vehicle.setServiceStatus(ServiceStatus.SCHEDULED);
//        vehicle.setQueueNumber(serviceAdvisor.getScheduledVehicleCount());
//        vehicle.setDeliveryTime(getExpectedDeliveryTime(vehicle.getQueueNumber()));
//        vehicleRepository.save(vehicle);
//        return ApiResponse.builder().message("Vehicle scheduled successfully.").status("Success").build();
//    }
    
    @Override
    @Transactional
    public ApiResponse scheduleVehicleForService(
            String vehicleNumber,
            Long serviceAdvisorId,
            Long scheduleId) {

        Vehicle vehicle = vehicleRepository
                .findByVehicleNumberIgnoreCase(vehicleNumber)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Vehicle",
                                "vehicleNumber",
                                vehicleNumber));

        User serviceAdvisor = userRepository
                .findByRoleAndId(
                        Role.SERVICE_ADVISOR,
                        serviceAdvisorId)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Service Advisor",
                                "id",
                                serviceAdvisorId.toString()));

        AdvisorSchedule schedule = advisorScheduleRepository
                .findById(scheduleId)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Schedule",
                                "id",
                                scheduleId.toString()));

        if (vehicle.getServiceStatus() != ServiceStatus.DUE) {

            return ApiResponse.builder()
                    .status("FAILED")
                    .message("Vehicle is not due for service")
                    .build();
        }

        if (schedule.getStatus() != ScheduleStatus.AVAILABLE) {

            return ApiResponse.builder()
                    .status("FAILED")
                    .message("Selected slot is not available")
                    .build();
        }

        // Assign vehicle
        vehicle.setServiceAdvisor(serviceAdvisor);
        vehicle.setServiceStatus(ServiceStatus.SCHEDULED);

        // Assign slot
        schedule.setVehicle(vehicle);
        schedule.setStatus(ScheduleStatus.BOOKED);

        vehicle.setQueueNumber(
                serviceAdvisor.getScheduledVehicleCount() + 1);

        vehicle.setDeliveryTime(
                getExpectedDeliveryTime(
                        vehicle.getQueueNumber()));

        advisorScheduleRepository.save(schedule);

        serviceAdvisor.setScheduledVehicleCount(
                serviceAdvisor.getScheduledVehicleCount() + 1);

        userRepository.save(serviceAdvisor);

        vehicleRepository.save(vehicle);

        return ApiResponse.builder()
                .status("SUCCESS")
                .message("Vehicle scheduled successfully")
                .build();
    }

//    @Override
//    public ApiResponse startVehicleService(String vehicleNumber) {
//        Vehicle vehicle = vehicleRepository.findByVehicleNumberIgnoreCase(vehicleNumber).orElseThrow(() -> new ResourceNotFoundException("Vehicle", "vehicleNumber", vehicleNumber));
//        if (!(vehicle.getServiceStatus() == ServiceStatus.SCHEDULED)) {
//            return ApiResponse.builder().message("Failed to process the request!! Try again.").status("Failed").build();
//        }
//        vehicle.setServiceStatus(ServiceStatus.UNDER_SERVICING);
//        vehicleRepository.save(vehicle);
//        return ApiResponse.builder().message("Service started for vehicle.").status("Success").build();
//    }
    
    
    
    @Override
    @Transactional
    public ApiResponse startVehicleService(String vehicleNumber) {

    	
    	System.out.println("Received Vehicle Number = [" + vehicleNumber + "]");
    	
    	
    	
        Vehicle vehicle = vehicleRepository
                .findByVehicleNumberIgnoreCase(vehicleNumber)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Vehicle",
                                "vehicleNumber",
                                vehicleNumber));

        if (vehicle.getServiceStatus() != ServiceStatus.SCHEDULED) {

            return ApiResponse.builder()
                    .status("Failed")
                    .message("Vehicle is not scheduled.")
                    .build();
        }

        AdvisorSchedule schedule = advisorScheduleRepository
                .findByVehicle(vehicle)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Schedule",
                                "vehicle",
                                vehicleNumber));

        vehicle.setServiceStatus(ServiceStatus.UNDER_SERVICING);

        schedule.setStatus(ScheduleStatus.IN_PROGRESS);
        schedule.setActualStartTime(LocalDateTime.now());

        vehicleRepository.save(vehicle);
        advisorScheduleRepository.save(schedule);

        return ApiResponse.builder()
                .status("Success")
                .message("Service started successfully.")
                .build();
    }

//    @Override
//    public ApiResponse completeVehicleService(String vehicleNumber, HashMap<Long, Integer> itemQuantityMap) {
//        Vehicle vehicle = vehicleRepository.findByVehicleNumberIgnoreCase(vehicleNumber).orElseThrow(() -> new ResourceNotFoundException("Vehicle", "vehicleNumber", vehicleNumber));
//        if (!(vehicle.getServiceStatus() == ServiceStatus.UNDER_SERVICING)) {
//            return ApiResponse.builder().message("Failed to process the request!! Try again.").status("Failed").build();
//        }
//        HashMap<Item, Integer> map = new HashMap<>();
//        double amount = 0d;
//        
//        
//        
////        for (Map.Entry<Long, Integer> entry : itemQuantityMap.entrySet()) {
////            Item item = itemRepository.findById(entry.getKey()).orElseThrow(() -> new ResourceNotFoundException("Item", "id", entry.getKey().toString()));
////            map.put(item, entry.getValue());
////            amount += item.getPrice() * map.get(item);
////        }
//        
//        
//        
//        for (Map.Entry<Long, Integer> entry : itemQuantityMap.entrySet()) {
//
//            Item item = itemRepository.findById(entry.getKey())
//                    .orElseThrow(() ->
//                            new ResourceNotFoundException(
//                                    "Item",
//                                    "id",
//                                    entry.getKey().toString()));
//
//            Integer requestedQuantity = entry.getValue();
//
//            // validate stock
//            if (item.getQuantity() < requestedQuantity) {
//
//                return ApiResponse.builder()
//                        .status("Failed")
//                        .message(
//                            "Insufficient stock for item: "
//                            + item.getName()
//                        )
//                        .build();
//            }
//
//            // reduce stock
//            item.setQuantity(
//                    item.getQuantity() - requestedQuantity
//            );
//
//            // save updated item quantity
//            itemRepository.save(item);
//
//            map.put(item, requestedQuantity);
//
//            amount += item.getPrice() * requestedQuantity;
//        }
//        
//        
//        
//        
//        ServiceRecord serviceRecord = ServiceRecord.builder()
//                .vehicle(vehicle)
//                .serviceAdvisor(vehicle.getServiceAdvisor())
//                .itemQuantityMap(map)
//                .amount(amount)
//                .serviceDate(LocalDateTime.now().plusHours(6))
//                .isAdminApproved(Boolean.FALSE)
//                .isPaymentCompleted(Boolean.FALSE)
//                .build();
//        vehicle.setServiceRecord(serviceRecord);
//        vehicle.setServiceStatus(ServiceStatus.SERVICED);
//        vehicleRepository.save(vehicle);
//        advisorService.updateAdvisorStatusAfterService(vehicle.getServiceAdvisor(), vehicle);
////        emailService.sendEmail(vehicle.getOwner().getEmail(), "Vehicle Service Status", "Dear Customer, \n\nYour vehicle has been serviced, and is ready for the delivery/pickup. Invoice will be mailed to you shortly.\n\nThank You !!");
//        return ApiResponse.builder().message("Vehicle service completed.").status("Success").build();
//    }
    
//    @Override
//    @Transactional
//    public ApiResponse completeVehicleService(String vehicleNumber,
//                                             HashMap<Long, Integer> itemQuantityMap) {
//
//        Vehicle vehicle = vehicleRepository.findByVehicleNumberIgnoreCase(vehicleNumber)
//                .orElseThrow(() ->
//                        new ResourceNotFoundException("Vehicle", "vehicleNumber", vehicleNumber));
//
//        if (vehicle.getServiceStatus() != ServiceStatus.UNDER_SERVICING) {
//            return ApiResponse.builder()
//                    .status("Failed")
//                    .message("Vehicle is not under servicing.")
//                    .build();
//        }
//
//        AdvisorSchedule schedule = advisorScheduleRepository
//                .findByVehicle(vehicle)
//                .orElseThrow(() ->
//                        new ResourceNotFoundException("Schedule", "vehicle", vehicleNumber));
//
//        Map<Item, Integer> itemMap = new HashMap<>();
//        double totalAmount = 0.0;
//
//        for (Map.Entry<Long, Integer> entry : itemQuantityMap.entrySet()) {
//
//            Item item = itemRepository.findById(entry.getKey())
//                    .orElseThrow(() ->
//                            new ResourceNotFoundException("Item", "id", entry.getKey().toString()));
//
//            itemMap.put(item, entry.getValue());
//            totalAmount += item.getPrice() * entry.getValue();
//        }
//
//        // ✅ CREATE SERVICE RECORD (HISTORY ENTRY)
//        ServiceRecord record = ServiceRecord.builder()
//                .vehicle(vehicle)
//                .serviceAdvisor(schedule.getServiceAdvisor())
//                .itemQuantityMap(itemMap)
//                .amount(totalAmount)
//                .serviceDate(LocalDateTime.now())
//                .isAdminApproved(false)
//                .isPaymentCompleted(false)
//                .build();
//
//        // 🔥 ADD TO HISTORY LIST
//        vehicle.getServiceRecords().add(record);
//
//        // ✅ UPDATE SCHEDULE
//        schedule.setActualEndTime(LocalDateTime.now());
//        schedule.setStatus(ScheduleStatus.COMPLETED);
//
//        // ✅ UPDATE VEHICLE STATUS
//        vehicle.setServiceStatus(ServiceStatus.SERVICED);
//
//        // SAVE ROOT ENTITY (cascade saves ServiceRecord)
//        vehicleRepository.save(vehicle);
//        advisorScheduleRepository.save(schedule);
//
//        // Advisor update
//        advisorService.updateAdvisorStatusAfterService(
//                schedule.getServiceAdvisor(),
//                vehicle
//        );
//
//        // Email
//        if (vehicle.getOwner() != null && vehicle.getOwner().getEmail() != null) {
//            emailService.sendEmail(
//                    vehicle.getOwner().getEmail(),
//                    "Service Completed",
//                    "Your vehicle " + vehicle.getVehicleNumber() + " is serviced successfully."
//            );
//        }
//
//        return ApiResponse.builder()
//                .status("Success")
//                .message("Service completed and history saved.")
//                .build();
//    }

    
    @Override
    public ApiResponse completeVehicleService(
            String vehicleNumber,
            HashMap<Long, Integer> itemQuantityMap
    ) {

        Vehicle vehicle = vehicleRepository.findByVehicleNumberIgnoreCase(vehicleNumber)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Vehicle",
                                "vehicleNumber",
                                vehicleNumber
                        )
                );

        if (vehicle.getServiceStatus() != ServiceStatus.UNDER_SERVICING) {
            return ApiResponse.builder()
                    .message("Failed to process the request!! Try again.")
                    .status("Failed")
                    .build();
        }

        HashMap<Item, Integer> map = new HashMap<>();
        double amount = 0d;

        for (Map.Entry<Long, Integer> entry : itemQuantityMap.entrySet()) {

            Long itemId = entry.getKey();
            Integer qty = entry.getValue();

            Item item = itemRepository.findById(itemId)
                    .orElseThrow(() ->
                            new ResourceNotFoundException(
                                    "Item",
                                    "id",
                                    itemId.toString()
                            )
                    );

            // 🔥 STOCK OUT via SERVICE (IMPORTANT CHANGE)
            stockService.addStockOut(
                    itemId,
                    qty,
                    "Used in vehicle service: " + vehicleNumber
            );

            map.put(item, qty);

            amount += item.getPrice() * qty;
        }

        ServiceRecord serviceRecord = ServiceRecord.builder()
                .vehicle(vehicle)
                .serviceAdvisor(vehicle.getServiceAdvisor())
                .itemQuantityMap(map)
                .amount(amount)
                .serviceDate(LocalDateTime.now().plusHours(6))
                .isAdminApproved(Boolean.FALSE)
                .isPaymentCompleted(Boolean.FALSE)
                .build();

        vehicle.setServiceRecord(serviceRecord);
        vehicle.setServiceStatus(ServiceStatus.SERVICED);

        vehicleRepository.save(vehicle);

        advisorService.updateAdvisorStatusAfterService(
                vehicle.getServiceAdvisor(),
                vehicle
        );

        return ApiResponse.builder()
                .message("Vehicle service completed.")
                .status("Success")
                .build();
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
    
    @Override
    public ApiResponse updateVehicle(Long id, VehicleDto dto) {

        // 🔹 Find existing vehicle
        Vehicle vehicle = vehicleRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Vehicle", "id", id.toString()));

        // 🔹 Duplicate check (vehicle number)
        vehicleRepository.findByVehicleNumberIgnoreCase(dto.getVehicleNumber())
                .filter(v -> !v.getId().equals(id))   // exclude current vehicle
                .ifPresent(v -> {
                    throw new RuntimeException("Vehicle number already exists");
                });

        // 🔹 Update fields
        vehicle.setVehicleModel(dto.getVehicleModel());
        vehicle.setVehicleNumber(dto.getVehicleNumber());
        vehicle.setVehicleDescription(dto.getVehicleDescription());

        // 🔹 Save updated vehicle
        vehicleRepository.save(vehicle);

        // 🔹 Return response
        return ApiResponse.builder()
                .message("Vehicle updated successfully")
                .status("SUCCESS")
                .build();
    }
    
    
//    @Transactional
//    private VehicleResponse buildVehicleResponse(Vehicle vehicle) {
//
//        VehicleResponse response = new VehicleResponse();
//
//        response.setId(vehicle.getId());
//        response.setVehicleNumber(vehicle.getVehicleNumber());
//        response.setVehicleModel(vehicle.getVehicleModel());
//        response.setVehicleDescription(vehicle.getVehicleDescription());
//        response.setServiceStatus(vehicle.getServiceStatus());
//
//        // Owner Details
//        if (vehicle.getOwner() != null) {
//            response.setOwner(
//                   
//                            vehicle.getOwner());
//        }
//
//        // Schedule Details
//        AdvisorSchedule schedule =
//                advisorScheduleRepository
//                        .findByVehicle(vehicle)
//                        .orElse(null);
//
//        if (schedule != null) {
//
//            response.setScheduleDate(
//                    schedule.getScheduleDate());
//
//            response.setStartTime(
//                    schedule.getStartTime());
//
//            response.setEndTime(
//                    schedule.getEndTime());
//
//            User advisorUser =
//                    schedule.getServiceAdvisor();
//
//            if (advisorUser != null) {
//
//                UserDto advisorUserDto =
//                        modelMapper.map(
//                                advisorUser,
//                                UserDto.class);
//
//                List<AdvisorScheduleDto> schedules =
//                        List.of(
//                                modelMapper.map(
//                                        schedule,
//                                        AdvisorScheduleDto.class));
//
//                ServiceAdvisorDto advisorDto =
//                        ServiceAdvisorDto.builder()
//                                .advisor(advisorUserDto)
//                                .schedules(schedules)
//                                .build();
//
//                response.setServiceAdvisor(advisorDto);
//            }
//        }
//
//        return response;
//    }
    
    
    private VehicleResponse buildVehicleResponse(Vehicle vehicle) {

        VehicleResponse response = VehicleResponse.builder()
                .id(vehicle.getId())
                .vehicleNumber(vehicle.getVehicleNumber())
                .vehicleModel(vehicle.getVehicleModel())
                .vehicleDescription(vehicle.getVehicleDescription())
                .owner(vehicle.getOwner())
                .serviceStatus(vehicle.getServiceStatus())
                .build();

        AdvisorSchedule schedule = advisorScheduleRepository
                .findByVehicle(vehicle)
                .orElse(null);

        if (schedule != null) {

            response.setScheduleDate(schedule.getScheduleDate());
            response.setStartTime(schedule.getStartTime());
            response.setEndTime(schedule.getEndTime());

            User advisor = schedule.getServiceAdvisor();

            if (advisor != null) {

                UserDto advisorDto = UserDto.builder()
                        .id(advisor.getId())
                        .firstName(advisor.getFirstName())
                        .lastName(advisor.getLastName())
                        .phone(advisor.getPhone())
                        .email(advisor.getEmail())
                        .address(advisor.getAddress())
                        .build();

                AdvisorScheduleDto scheduleDto =
                        modelMapper.map(
                                schedule,
                                AdvisorScheduleDto.class);

                ServiceAdvisorDto serviceAdvisorDto =
                        ServiceAdvisorDto.builder()
                                .advisor(advisorDto)
                                .schedules(List.of(scheduleDto))
                                .build();

                response.setServiceAdvisor(serviceAdvisorDto);
            }
        }

        return response;
    }
}
