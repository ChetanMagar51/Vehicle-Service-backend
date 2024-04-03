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
    public ApiResponse register(RegisterRequest registerRequest) {
        User user = User.builder()
                .firstName(registerRequest.getFirstName())
                .lastName(registerRequest.getLastName())
                .phone(registerRequest.getPhone())
                .address(registerRequest.getAddress())
                .email(registerRequest.getEmail())
                .password(passwordEncoder.encode(registerRequest.getPassword()))
                .role(Role.SERVICE_ADVISOR)
                .build();
        userRepository.save(user);
        return ApiResponse.builder()
                .message("User registered successfully !!")
                .status("Success")
                .build();
    }

    @Override
    public AuthenticationResponse authenticate(AuthenticationRequest authenticationRequest) {
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(authenticationRequest.getEmail(), authenticationRequest.getPassword()));
        user = userRepository.findByEmail(authenticationRequest.getEmail()).orElseThrow(() -> new ResourceNotFoundException("User", "userEmail", authenticationRequest.getEmail()));
        String jwtToken = jwtUtil.generateToken(user);
        return AuthenticationResponse.builder().token(jwtToken).build();
    }

    @Override
    public UserDto getCurrentUser() {
        return modelMapper.map(user, UserDto.class);
    }
}
