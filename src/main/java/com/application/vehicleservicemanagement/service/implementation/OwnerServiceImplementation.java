package com.application.vehicleservicemanagement.service.implementation;

import com.application.vehicleservicemanagement.dto.ApiResponse;
import com.application.vehicleservicemanagement.dto.UserDto;
import com.application.vehicleservicemanagement.dto.VehicleDto;
import com.application.vehicleservicemanagement.entity.Owner;
import com.application.vehicleservicemanagement.entity.Vehicle;
import com.application.vehicleservicemanagement.exception.ResourceNotFoundException;
import com.application.vehicleservicemanagement.repository.OwnerRepository;
import com.application.vehicleservicemanagement.service.OwnerService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class OwnerServiceImplementation implements OwnerService {
    private final OwnerRepository ownerRepository;
    private final ModelMapper modelMapper;

    @Override
    public ApiResponse addOwner(UserDto userDto) {
        Owner owner = Owner.builder()
                .firstName(userDto.getFirstName())
                .lastName(userDto.getLastName())
                .email(userDto.getEmail())
                .phone(userDto.getPhone())
                .address(userDto.getAddress())
                .build();
        ownerRepository.save(owner);
        return ApiResponse.builder().message("Owner added successfully.").status("Success").build();
    }

    @Override
    public UserDto getOwnerById(Long id) {
        Owner owner = ownerRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Owner", "id", id.toString()));
        return modelMapper.map(owner, UserDto.class);
    }

    @Override
    public UserDto getOwnerByEmail(String email) {
        Owner owner = ownerRepository.findByEmail(email).orElseThrow(() -> new ResourceNotFoundException("Owner", "email", email));
        return modelMapper.map(owner, UserDto.class);
    }

    @Override
    public UserDto getOwnerByPhone(String phone) {
        Owner owner = ownerRepository.findByPhone(phone).orElseThrow(() -> new ResourceNotFoundException("Owner", "phone", phone));
        return modelMapper.map(owner, UserDto.class);
    }

    @Override
    public List<UserDto> getAllOwners() {
        List<Owner> owners = ownerRepository.findAll();
        return owners.stream().map(owner -> modelMapper.map(owner, UserDto.class)).toList();
    }

    @Override
    public List<VehicleDto> getAllVehiclesOfOwner(Long id) {
        Owner owner = ownerRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Owner", "id", id.toString()));
        List<Vehicle> vehicles = owner.getVehicleList();
        return vehicles.stream().map(vehicle -> modelMapper.map(vehicle, VehicleDto.class)).toList();
    }

    @Override
    public ApiResponse updateOwnerById(Long id, UserDto userDto) {
        Owner owner = ownerRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Owner", "id", id.toString()));
        return updateOwner(userDto, owner);
    }

    @Override
    public ApiResponse updateOwnerByPhone(String phone, UserDto userDto) {
        Owner owner = ownerRepository.findByPhone(phone).orElseThrow(() -> new ResourceNotFoundException("Owner", "phone", phone));
        return updateOwner(userDto, owner);
    }

    @Override
    public ApiResponse deleteOwnerById(Long id) {
        ownerRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Owner", "id", id.toString()));
        ownerRepository.deleteById(id);
        return ApiResponse.builder().message("Owner deleted successfully.").status("Success").build();
    }

    private ApiResponse updateOwner(UserDto userDto, Owner owner) {
        owner.setFirstName(userDto.getFirstName());
        owner.setLastName(userDto.getLastName());
        owner.setPhone(userDto.getPhone());
        owner.setAddress(userDto.getAddress());
        owner.setEmail(userDto.getEmail());
        ownerRepository.save(owner);
        return ApiResponse.builder().message("Owner updated successfully.").status("Success").build();
    }
}
