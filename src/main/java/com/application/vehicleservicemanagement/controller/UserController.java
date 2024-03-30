package com.application.vehicleservicemanagement.controller;

import com.application.vehicleservicemanagement.dto.ApiResponseDTO;
import com.application.vehicleservicemanagement.dto.UserDTO;
import com.application.vehicleservicemanagement.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/vsm/user")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @GetMapping("/get")
    public ResponseEntity<UserDTO> getUser(@RequestParam(value = "id") Long id) {
        return ResponseEntity.ok(userService.getUserById(id));
    }

    @GetMapping("/get/all")
    public ResponseEntity<List<UserDTO>> getAllUsers() {
        return ResponseEntity.ok(userService.getAllUsers());
    }

    @GetMapping("/get/all/serviceAdvisor")
    public ResponseEntity<List<UserDTO>> getAllServiceAdvisors() {
        return ResponseEntity.ok(userService.getAllServiceAdvisors());
    }

    @PatchMapping("/update")
    public ResponseEntity<ApiResponseDTO> updateUserById(@RequestParam(value = "id") Long id, @RequestBody UserDTO userDTO) {
        return ResponseEntity.ok(userService.updateUserById(id, userDTO));
    }

    @DeleteMapping("/delete")
    public ResponseEntity<ApiResponseDTO> deleteUser(@RequestParam(value = "id") Long id) {
        return ResponseEntity.ok(userService.deleteUserById(id));
    }
}
