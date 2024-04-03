package com.application.vehicleservicemanagement.service;

import com.application.vehicleservicemanagement.dto.*;

public interface AuthenticationService {
    ApiResponseDTO register(RegisterRequestDTO registerRequestDTO);

    AuthenticationResponseDTO authenticate(AuthenticationRequestDTO authenticationRequestDTO);

    UserDTO getCurrentUser();
}
