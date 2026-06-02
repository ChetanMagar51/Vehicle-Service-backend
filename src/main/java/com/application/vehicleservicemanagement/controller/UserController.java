package com.application.vehicleservicemanagement.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.application.vehicleservicemanagement.dto.ApiResponse;
import com.application.vehicleservicemanagement.dto.ServiceAdvisorDto;
import com.application.vehicleservicemanagement.dto.UserDto;
import com.application.vehicleservicemanagement.service.UserService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @GetMapping("/get")
    public ResponseEntity<UserDto> getUser(@RequestParam(value = "id") Long id) {
        return new ResponseEntity<>(userService.getUserById(id), HttpStatus.FOUND);
    }

    @GetMapping("/get/all")
    public ResponseEntity<List<UserDto>> getAllUsers() {
        return ResponseEntity.ok(userService.getAllUsers());
    }

    @GetMapping("/get/all/serviceAdvisor")
    public ResponseEntity<List<ServiceAdvisorDto>> getAllServiceAdvisors() {
        return ResponseEntity.ok(userService.getAllServiceAdvisors());
    }

    @PatchMapping("/update")
    public ResponseEntity<ApiResponse> updateUserById(@RequestParam(value = "id") Long id, @RequestBody UserDto userDTO) {
        return new ResponseEntity<>(userService.updateUserById(id, userDTO), HttpStatus.OK);
    }

    @DeleteMapping("/delete")
    public ResponseEntity<ApiResponse> deleteUser(@RequestParam(value = "id") Long id) {
        return new ResponseEntity<>(userService.deleteUserById(id), HttpStatus.OK);
    }
}
