package com.application.vehicleservicemanagement.service.implementation;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import com.application.vehicleservicemanagement.dto.ApiResponse;
import com.application.vehicleservicemanagement.dto.UserDto;
import com.application.vehicleservicemanagement.dto.VehicleDto;
import com.application.vehicleservicemanagement.entity.Owner;
import com.application.vehicleservicemanagement.entity.Vehicle;
import com.application.vehicleservicemanagement.repository.OwnerRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.modelmapper.ModelMapper;

public class OwnerServiceImplementationTest {

    @Mock
    private OwnerRepository ownerRepository;

    @Mock
    private ModelMapper modelMapper;

    @InjectMocks
    private OwnerServiceImplementation ownerService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testAddOwner() {
        UserDto userDto = new UserDto();
        userDto.setFirstName("John");
        userDto.setEmail("johndoe@gmail.com");
        userDto.setPhone("9876543210");
        userDto.setAddress("Pune,Maharashtra");

        Owner owner = new Owner();
        owner.setFirstName(userDto.getFirstName());
        owner.setLastName(userDto.getLastName());
        owner.setEmail(userDto.getEmail());
        owner.setPhone(userDto.getPhone());
        owner.setAddress(userDto.getAddress());

        when(ownerRepository.save(owner)).thenReturn(owner);
        when(modelMapper.map(any(UserDto.class), eq(Owner.class))).thenReturn(owner);

        ApiResponse response = ownerService.addOwner(userDto);

        assertNotNull(response);
        assertEquals("Owner added successfully.", response.getMessage());
        assertEquals("Success", response.getStatus());
        verify(ownerRepository, times(1)).save(owner);
    }

    @Test
    public void testGetOwnerById() {
        Long ownerId = 1L;
        Owner owner = new Owner();
        when(ownerRepository.findById(ownerId)).thenReturn(Optional.of(owner));
        when(modelMapper.map(owner, UserDto.class)).thenReturn(new UserDto());

        UserDto ownerDto = ownerService.getOwnerById(ownerId);

        assertNotNull(ownerDto);
        verify(ownerRepository, times(1)).findById(ownerId);
    }

    @Test
    public void testGetOwnerByEmail() {
        String ownerEmail = "johndoe@gmail.com";
        Owner owner = new Owner();
        when(ownerRepository.findByEmail(ownerEmail)).thenReturn(Optional.of(owner));
        when(modelMapper.map(owner, UserDto.class)).thenReturn(new UserDto());

        UserDto ownerDto = ownerService.getOwnerByEmail(ownerEmail);

        assertNotNull(ownerDto);
        verify(ownerRepository, times(1)).findByEmail(ownerEmail);
    }

    @Test
    public void testGetOwnerByPhone() {
        String ownerPhone = "9876543210";
        Owner owner = new Owner();
        when(ownerRepository.findByPhone(ownerPhone)).thenReturn(Optional.of(owner));
        when(modelMapper.map(owner, UserDto.class)).thenReturn(new UserDto());

        UserDto ownerDto = ownerService.getOwnerByPhone(ownerPhone);

        assertNotNull(ownerDto);
        verify(ownerRepository, times(1)).findByPhone(ownerPhone);
    }

    @Test
    public void testGetAllOwners() {
        List<Owner> owners = new ArrayList<>();
        owners.add(new Owner());
        when(ownerRepository.findAll()).thenReturn(owners);
        when(modelMapper.map(any(Owner.class), eq(UserDto.class))).thenReturn(new UserDto());

        List<UserDto> ownerDtos = ownerService.getAllOwners();

        assertNotNull(ownerDtos);
        assertEquals(1, ownerDtos.size());
        verify(ownerRepository, times(1)).findAll();
    }

    @Test
    public void testGetAllVehiclesOfOwner() {
        Long ownerId = 1L;
        Owner owner = new Owner();
        owner.setId(ownerId);
        when(ownerRepository.findById(ownerId)).thenReturn(Optional.of(owner));

        List<Vehicle> vehicles = new ArrayList<>();
        vehicles.add(new Vehicle());
        owner.setVehicleList(vehicles);
        when(modelMapper.map(any(Vehicle.class), eq(VehicleDto.class))).thenReturn(new VehicleDto());

        List<VehicleDto> vehicleDtos = ownerService.getAllVehiclesOfOwner(ownerId);

        assertNotNull(vehicleDtos);
        assertEquals(1, vehicleDtos.size());
        verify(ownerRepository, times(1)).findById(ownerId);
    }

    @Test
    public void testUpdateOwnerById() {
        Long ownerId = 1L;
        UserDto userDto = new UserDto();
        userDto.setFirstName("John");
        userDto.setEmail("johndoe@gmail.com");
        userDto.setPhone("9876543210");
        userDto.setAddress("Pune,Maharashtra");

        Owner owner = new Owner();
        owner.setId(ownerId);
        when(ownerRepository.findById(ownerId)).thenReturn(Optional.of(owner));

        ApiResponse response = ownerService.updateOwnerById(ownerId, userDto);

        assertNotNull(response);
        assertEquals("Owner updated successfully.", response.getMessage());
        assertEquals("Success", response.getStatus());
        verify(ownerRepository, times(1)).save(owner);
    }

    @Test
    public void testUpdateOwnerByPhone() {

        String phone = "9876543210";
        UserDto userDto = new UserDto();
        userDto.setFirstName("John");
        userDto.setEmail("johndoe@gmail.com");
        userDto.setPhone("9876543210");
        userDto.setAddress("Pune,Maharashtra");

        Owner owner = new Owner();
        when(ownerRepository.findByPhone(phone)).thenReturn(Optional.of(owner));

        ApiResponse response = ownerService.updateOwnerByPhone(phone, userDto);

        assertNotNull(response);
        assertEquals("Owner updated successfully.", response.getMessage());
        assertEquals("Success", response.getStatus());
        verify(ownerRepository, times(1)).save(owner);
    }

    @Test
    public void testUpdateOwner() {
        Long ownerId = 1L;
        UserDto userDto = new UserDto();
        userDto.setFirstName("John");
        userDto.setEmail("johndoe@gmail.com");
        userDto.setPhone("9876543210");
        userDto.setAddress("Pune,Maharashtra");

        Owner owner = new Owner();
        owner.setId(ownerId);
        when(ownerRepository.findById(ownerId)).thenReturn(Optional.of(owner));

        ApiResponse response = ownerService.updateOwnerById(ownerId, userDto);

        assertNotNull(response);
        assertEquals("Owner updated successfully.", response.getMessage());
        assertEquals("Success", response.getStatus());
        verify(ownerRepository, times(1)).save(owner);
    }
}
