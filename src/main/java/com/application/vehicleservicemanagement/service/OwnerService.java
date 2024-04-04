package com.application.vehicleservicemanagement.service;

import com.application.vehicleservicemanagement.dto.ApiResponse;
import com.application.vehicleservicemanagement.dto.UserDto;
import com.application.vehicleservicemanagement.dto.VehicleDto;

import java.util.List;

public interface OwnerService {
    ApiResponse addOwner(UserDto userDto);

    UserDto getOwnerById(Long id);

    UserDto getOwnerByEmail(String email);

    UserDto getOwnerByPhone(String phone);

    List<UserDto> getAllOwners();

    List<VehicleDto> getAllVehiclesOfOwner(Long id);
}
