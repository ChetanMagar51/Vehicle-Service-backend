package com.application.vehicleservicemanagement.service.implementation;

import com.application.vehicleservicemanagement.dto.*;
import com.application.vehicleservicemanagement.entity.Role;
import com.application.vehicleservicemanagement.entity.User;
import com.application.vehicleservicemanagement.exception.ResourceNotFoundException;
import com.application.vehicleservicemanagement.repository.UserRepository;
import com.application.vehicleservicemanagement.service.AuthenticationService;
import com.application.vehicleservicemanagement.util.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthenticationServiceImplementation implements AuthenticationService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;
    private final AuthenticationManager authenticationManager;
    private final ModelMapper modelMapper;

    private User user;

    @Override
    public ApiResponseDTO register(RegisterRequestDTO registerRequestDTO) {
        User user = User.builder()
                .firstName(registerRequestDTO.getFirstName())
                .lastName(registerRequestDTO.getLastName())
                .phone(registerRequestDTO.getPhone())
                .address(registerRequestDTO.getAddress())
                .email(registerRequestDTO.getEmail())
                .password(passwordEncoder.encode(registerRequestDTO.getPassword()))
                .role(Role.SERVICE_ADVISOR)
                .build();
        userRepository.save(user);
        return ApiResponseDTO.builder()
                .message("User registered successfully !!")
                .status("Success")
                .build();
    }

    @Override
    public AuthenticationResponseDTO authenticate(AuthenticationRequestDTO authenticationRequestDTO) {
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(authenticationRequestDTO.getEmail(), authenticationRequestDTO.getPassword()));
        user = userRepository.findByEmail(authenticationRequestDTO.getEmail()).orElseThrow(() -> new ResourceNotFoundException("User", "userEmail", authenticationRequestDTO.getEmail()));
        String jwtToken = jwtUtil.generateToken(user);
        return AuthenticationResponseDTO.builder().token(jwtToken).build();
    }

    @Override
    public UserDTO getCurrentUser() {
        return modelMapper.map(user, UserDTO.class);
    }
}
