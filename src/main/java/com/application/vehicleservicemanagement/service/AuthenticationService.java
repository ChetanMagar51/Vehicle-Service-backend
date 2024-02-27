package com.application.vehicleservicemanagement.service;

import com.application.vehicleservicemanagement.dto.ApiResponseDTO;
import com.application.vehicleservicemanagement.dto.AuthenticationRequestDTO;
import com.application.vehicleservicemanagement.dto.AuthenticationResponseDTO;
import com.application.vehicleservicemanagement.dto.RegisterRequestDTO;

public interface AuthenticationService {
    ApiResponseDTO register(RegisterRequestDTO registerRequestDTO);

    AuthenticationResponseDTO authenticate(AuthenticationRequestDTO authenticationRequestDTO);
}
