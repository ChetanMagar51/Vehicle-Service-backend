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

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.modelmapper.ModelMapper;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class VehicleServiceImplementationTest {

    @Mock
    private VehicleRepository vehicleRepository;

    @Mock
    private OwnerRepository ownerRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private ItemRepository itemRepository;

    @Mock
    private AdvisorService advisorService;

    @Mock
    private ModelMapper modelMapper;

    @InjectMocks
    private VehicleServiceImplementation vehicleService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testRegisterVehicle(){
        VehicleDto vehicleDto = new VehicleDto();
        vehicleDto.setOwnerId(1L);
        Owner owner = new Owner();
        when(ownerRepository.findById(1L)).thenReturn(Optional.of(owner));
        when(vehicleRepository.save(any(Vehicle.class))).thenReturn(new Vehicle());

        ApiResponse response = vehicleService.registerVehicle(vehicleDto);

        assertEquals("Vehicle registered successfully.", response.getMessage());
        assertEquals("Success", response.getStatus());
    }

    @Test
    void tesGetVehicleById(){
        Long vehicleId = 1L;
        Vehicle vehicle = new Vehicle();
        when(vehicleRepository.findById(vehicleId)).thenReturn(Optional.of(vehicle));
        when(modelMapper.map(vehicle, VehicleResponse.class)).thenReturn(new VehicleResponse());

        VehicleResponse response = vehicleService.getVehicleById(vehicleId);

        assertNotNull(response);
    }

    @Test
    void testGetVehicleByVehicleNumber(){
        String vehicleNumber = "ABC123";
        Vehicle vehicle = new Vehicle();
        when(vehicleRepository.findByVehicleNumberIgnoreCase(vehicleNumber)).thenReturn(Optional.of(vehicle));
        when(modelMapper.map(vehicle, VehicleResponse.class)).thenReturn(new VehicleResponse());

        VehicleResponse response = vehicleService.getVehicleByVehicleNumber(vehicleNumber);

        assertNotNull(response);
    }

    @Test
    void testGetVehicleByVehicleNumberNotPresent(){
        String vehicleNumber = "XYZ789";
        when(vehicleRepository.findByVehicleNumberIgnoreCase(vehicleNumber)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> vehicleService.getVehicleByVehicleNumber(vehicleNumber));
    }

    @Test
    void testGetAllVehicles(){
        List<Vehicle> vehicleList = new ArrayList<>();
        vehicleList.add(new Vehicle());
        vehicleList.add(new Vehicle());
        when(vehicleRepository.findAll()).thenReturn(vehicleList);
        when(modelMapper.map(any(Vehicle.class), eq(VehicleResponse.class))).thenReturn(new VehicleResponse());

        List<VehicleResponse> responseList = vehicleService.getAllVehicles();

        assertNotNull(responseList);
        assertFalse(responseList.isEmpty());
        assertEquals(vehicleList.size(), responseList.size());
    }

    //Test when list of vehicles is empty
    @Test
    void testGetAllVehiclesEmptyList(){
        when(vehicleRepository.findAll()).thenReturn(new ArrayList<>());

        List<VehicleResponse> responseList = vehicleService.getAllVehicles();

        assertNotNull(responseList);
        assertTrue(responseList.isEmpty());
    }

    @Test
    void testGetAllDueVehicles(){
        when(vehicleRepository.findAllByServiceStatus(ServiceStatus.DUE)).thenReturn(new ArrayList<>());

        List<VehicleResponse> responseList = vehicleService.getAllDueVehicles();

        assertNotNull(responseList);
        assertTrue(responseList.isEmpty());
    }

    @Test
    void testGetAllScheduledVehicles(){
        when(vehicleRepository.findAllByServiceStatus(ServiceStatus.DUE)).thenReturn(new ArrayList<>());

        List<VehicleResponse> responseList = vehicleService.getAllScheduledVehicles();

        assertNotNull(responseList);
        assertTrue(responseList.isEmpty());
    }

    @Test
    void testGetAllVehiclesUnderServicing(){
        when(vehicleRepository.findAllByServiceStatus(ServiceStatus.DUE)).thenReturn(new ArrayList<>());

        List<VehicleResponse> responseList = vehicleService.getAllVehiclesUnderServicing();

        assertNotNull(responseList);
        assertTrue(responseList.isEmpty());
    }

    @Test
    void testGetAllServicedVehicles(){
        when(vehicleRepository.findAllByServiceStatus(ServiceStatus.DUE)).thenReturn(new ArrayList<>());

        List<VehicleResponse> responseList = vehicleService.getAllServicedVehicles();

        assertNotNull(responseList);
        assertTrue(responseList.isEmpty());
    }

    @Test
    void testScheduleVehicleForService(){
        String vehicleNumber = "ABC123";
        Long serviceAdvisorId = 1L;
        Vehicle vehicle = new Vehicle();
        User serviceAdvisor = new User();
        serviceAdvisor.setId(serviceAdvisorId);
        when(vehicleRepository.findByVehicleNumberIgnoreCase(vehicleNumber)).thenReturn(Optional.of(vehicle));
        when(userRepository.findByRoleAndId(Role.SERVICE_ADVISOR, serviceAdvisorId)).thenReturn(Optional.of(serviceAdvisor));
        when(advisorService.updateAdvisorStatusDuringScheduling(serviceAdvisor)).thenReturn(true);

        ApiResponse response = vehicleService.scheduleVehicleForService(vehicleNumber, serviceAdvisorId);

        assertNotNull(response);
//        assertEquals("Vehicle scheduled successfully.", response.getMessage());
//        assertEquals("Success", response.getStatus());
    }

    @Test
    void testStartVehicleService(){
        String vehicleNumber = "ABC123";
        Vehicle vehicle = new Vehicle();
        vehicle.setServiceStatus(ServiceStatus.SCHEDULED);
        when(vehicleRepository.findByVehicleNumberIgnoreCase(vehicleNumber)).thenReturn(Optional.of(vehicle));

        ApiResponse response = vehicleService.startVehicleService(vehicleNumber);

        assertNotNull(response);
        assertEquals("Service started for vehicle.", response.getMessage());
        assertEquals("Success", response.getStatus());
    }

    @Test
    void testCompleteVehicleService(){
        String vehicleNumber = "ABC123";

        List<Long> itemIdList = new ArrayList<>();
        itemIdList.add(1L);
        Vehicle vehicle = new Vehicle();
        List<Optional<Item>> itemList = new ArrayList<>();

        Item item = new Item();
        item.setId(1L);
        item.setName("Oil Change");
        item.setPrice(50.0);
        itemList.add(Optional.of(item));

        when(vehicleRepository.findByVehicleNumberIgnoreCase(vehicleNumber)).thenReturn(Optional.of(vehicle));
        when(itemRepository.findByNameIgnoreCase(anyString())).thenReturn(Optional.of(new Item()));
        when(modelMapper.map(any(Item.class), eq(Item.class))).thenReturn(new Item());
        when(advisorService.updateAdvisorStatusDuringScheduling(any(User.class))).thenReturn(true);

        ApiResponse response = vehicleService.completeVehicleService(vehicleNumber, itemIdList);

        assertNotNull(response);
//        assertEquals("Vehicle service completed.", response.getMessage()); // Correct expected message
//        assertEquals("Success", response.getStatus());
    }

}
