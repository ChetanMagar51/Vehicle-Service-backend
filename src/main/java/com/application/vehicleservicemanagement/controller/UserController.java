package com.application.vehicleservicemanagement.controller;

import com.application.vehicleservicemanagement.dto.ApiResponse;
import com.application.vehicleservicemanagement.dto.UserDto;
import com.application.vehicleservicemanagement.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @GetMapping("/get")
    public ResponseEntity<UserDto> getUser(@RequestParam(value = "id") Long id) {
        return ResponseEntity.ok(userService.getUserById(id));
    }

    @GetMapping("/get/all")
    public ResponseEntity<List<UserDto>> getAllUsers() {
        return ResponseEntity.ok(userService.getAllUsers());
    }

    @GetMapping("/get/all/serviceAdvisor")
    public ResponseEntity<List<UserDto>> getAllServiceAdvisors() {
        return ResponseEntity.ok(userService.getAllServiceAdvisors());
    }

    @PatchMapping("/update")
    public ResponseEntity<ApiResponse> updateUserById(@RequestParam(value = "id") Long id, @RequestBody UserDto userDTO) {
        return ResponseEntity.ok(userService.updateUserById(id, userDTO));
    }

    @DeleteMapping("/delete")
    public ResponseEntity<ApiResponse> deleteUser(@RequestParam(value = "id") Long id) {
        return ResponseEntity.ok(userService.deleteUserById(id));
    }
}
