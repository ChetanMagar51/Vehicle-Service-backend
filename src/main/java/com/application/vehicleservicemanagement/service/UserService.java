package com.application.vehicleservicemanagement.service;

import com.application.vehicleservicemanagement.dto.ApiResponseDTO;
import com.application.vehicleservicemanagement.dto.UserDTO;

import java.util.List;

public interface UserService {
    UserDTO getUserById(Long id);

    List<UserDTO> getAllUsers();

    ApiResponseDTO updateUserById(Long id, UserDTO userDTO);

    ApiResponseDTO deleteUserById(Long id);

    List<UserDTO> getAllServiceAdvisors();
}
