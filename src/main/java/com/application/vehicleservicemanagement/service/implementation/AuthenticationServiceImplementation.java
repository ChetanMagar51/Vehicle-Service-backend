package com.application.vehicleservicemanagement.service.implementation;

import java.time.LocalDateTime;
import java.util.Objects;

import org.modelmapper.ModelMapper;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;

import com.application.vehicleservicemanagement.dto.AdminRegisterRequest;
import com.application.vehicleservicemanagement.dto.ApiResponse;
import com.application.vehicleservicemanagement.dto.AuthenticationRequest;
import com.application.vehicleservicemanagement.dto.AuthenticationResponse;
import com.application.vehicleservicemanagement.dto.RegisterRequest;
import com.application.vehicleservicemanagement.dto.UserDto;
import com.application.vehicleservicemanagement.entity.User;
import com.application.vehicleservicemanagement.exception.InvalidRoleException;
import com.application.vehicleservicemanagement.exception.ResourceNotFoundException;
import com.application.vehicleservicemanagement.repository.UserRepository;
import com.application.vehicleservicemanagement.service.AuthenticationService;
import com.application.vehicleservicemanagement.service.UserService;
import com.application.vehicleservicemanagement.util.JwtUtil;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AuthenticationServiceImplementation implements AuthenticationService {
    private final UserService userService;
    private final UserRepository userRepository;
    private final JwtUtil jwtUtil;
    private final AuthenticationManager authenticationManager;
    private final ModelMapper modelMapper;

    @Override
    public ApiResponse register(RegisterRequest registerRequest) {
        return userService.createUser(registerRequest);
    }

    @Override
    public AuthenticationResponse authenticate(AuthenticationRequest authenticationRequest) {
        String reqEmail = authenticationRequest.getEmail();
        String reqPassword = authenticationRequest.getPassword();
        User user = userRepository.findByEmail(authenticationRequest.getEmail()).orElseThrow(() -> new ResourceNotFoundException("User", "userEmail", reqEmail));
        if (!(Objects.equals(authenticationRequest.getType(), user.getRole().name()))) {
            throw new InvalidRoleException(user.getRole().name());
        }
        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(reqEmail, reqPassword));
        } catch (BadCredentialsException e) {
            throw new BadCredentialsException("Invalid credentials !! Please try again.");
        }
        String jwtToken = jwtUtil.generateToken(user);
        return AuthenticationResponse.builder()
                .status("Success")
                .userDto(modelMapper.map(user, UserDto.class))
                .token(jwtToken)
                .issuedAt(LocalDateTime.now())
                .expiration(jwtUtil.extractExpiration(jwtToken))
                .build();
    }
    
    @Override
    public ApiResponse registerAdmin(AdminRegisterRequest registerRequest) {
        return userService.createUserAdmin(registerRequest);
    }
    
    
}
