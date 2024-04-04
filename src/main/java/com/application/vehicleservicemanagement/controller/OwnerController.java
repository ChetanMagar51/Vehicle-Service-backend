package com.application.vehicleservicemanagement.controller;

import com.application.vehicleservicemanagement.dto.ApiResponse;
import com.application.vehicleservicemanagement.dto.UserDto;
import com.application.vehicleservicemanagement.dto.VehicleDto;
import com.application.vehicleservicemanagement.service.OwnerService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/owner")
@RequiredArgsConstructor
public class OwnerController {
    private final OwnerService ownerService;

    @PostMapping("/add")
    public ResponseEntity<ApiResponse> addOwner(@RequestBody UserDto userDto) {
        return ResponseEntity.ok(ownerService.addOwner(userDto));
    }

    @GetMapping("/get")
    public ResponseEntity<UserDto> getOwnerById(@RequestParam(value = "id") Long id) {
        return ResponseEntity.ok(ownerService.getOwnerById(id));
    }

    @GetMapping("/get/")
    public ResponseEntity<UserDto> getOwnerByPhone(@RequestBody String phone) {
        return ResponseEntity.ok(ownerService.getOwnerByPhone(phone));
    }

    @GetMapping("/get/all")
    public ResponseEntity<List<UserDto>> getAllOwners() {
        return ResponseEntity.ok(ownerService.getAllOwners());
    }

    @GetMapping("/get/vehiclesOwned")
    public ResponseEntity<List<VehicleDto>> getAllVehiclesOfOwner(@RequestParam(value = "id") Long id) {
        return ResponseEntity.ok(ownerService.getAllVehiclesOfOwner(id));
    }
}
