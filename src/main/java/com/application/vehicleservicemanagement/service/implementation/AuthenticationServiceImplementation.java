package com.application.vehicleservicemanagement.service.implementation;

import com.application.vehicleservicemanagement.dto.*;
import com.application.vehicleservicemanagement.entity.Role;
import com.application.vehicleservicemanagement.entity.User;
import com.application.vehicleservicemanagement.exception.CommonException;
import com.application.vehicleservicemanagement.exception.ResourceNotFoundException;
import com.application.vehicleservicemanagement.repository.UserRepository;
import com.application.vehicleservicemanagement.service.AuthenticationService;
import com.application.vehicleservicemanagement.service.UserService;
import com.application.vehicleservicemanagement.util.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class AuthenticationServiceImplementation implements AuthenticationService {
    private final UserService userService;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;
    private final AuthenticationManager authenticationManager;
    private final ModelMapper modelMapper;

    private User user;

    @Override
    public ApiResponse register(RegisterRequest registerRequest) {
        return userService.createUser(registerRequest);
    }

    @Override
    public AuthenticationResponse authenticate(AuthenticationRequest authenticationRequest) {
        String reqEmail = authenticationRequest.getEmail();
        String reqPassword = authenticationRequest.getPassword();
        User temp = userRepository.findByEmail(authenticationRequest.getEmail()).orElseThrow(() -> new ResourceNotFoundException("User", "userEmail", reqEmail));
        if (!(Objects.equals(authenticationRequest.getType(), temp.getRole().name()))) {
            throw new CommonException("Access requested by invalid Role !! Try again.");
        }
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(reqEmail, reqPassword));
        user = userRepository.findByEmail(authenticationRequest.getEmail()).orElseThrow(() -> new ResourceNotFoundException("User", "userEmail", reqEmail));
        String jwtToken = jwtUtil.generateToken(user);
        return AuthenticationResponse.builder()
                .status("Success")
                .token(jwtToken)
                .issuedAt(LocalDateTime.now())
                .expiration(jwtUtil.extractExpiration(jwtToken))
                .build();
    }

    @Override
    public UserDto getCurrentUser() {
        return modelMapper.map(user, UserDto.class);
    }
}
