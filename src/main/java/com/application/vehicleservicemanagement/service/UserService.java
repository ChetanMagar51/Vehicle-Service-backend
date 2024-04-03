package com.application.vehicleservicemanagement.service;

import com.application.vehicleservicemanagement.dto.ApiResponse;
import com.application.vehicleservicemanagement.dto.UserDto;

import java.util.List;

public interface UserService {
    UserDto getUserById(Long id);

    List<UserDto> getAllUsers();

    List<UserDto> getAllServiceAdvisors();

    ApiResponse updateUserById(Long id, UserDto userDTO);

    ApiResponse deleteUserById(Long id);
}
