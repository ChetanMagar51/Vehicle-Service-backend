package com.application.vehicleservicemanagement.controller;

import com.application.vehicleservicemanagement.dto.ApiResponse;
import com.application.vehicleservicemanagement.dto.UserDto;
import com.application.vehicleservicemanagement.dto.VehicleDto;
import com.application.vehicleservicemanagement.service.OwnerService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
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
        return new ResponseEntity<>(ownerService.addOwner(userDto), HttpStatus.CREATED);
    }

    @GetMapping("/get")
    public ResponseEntity<UserDto> getOwnerById(@RequestParam(value = "id") Long id) {
        return new ResponseEntity<>(ownerService.getOwnerById(id), HttpStatus.FOUND);
    }

    @GetMapping("/get/")
    public ResponseEntity<UserDto> getOwnerByPhone(@RequestBody String phone) {
        return new ResponseEntity<>(ownerService.getOwnerByPhone(phone), HttpStatus.FOUND);
    }

    @GetMapping("/get/all")
    public ResponseEntity<List<UserDto>> getAllOwners() {
        return ResponseEntity.ok(ownerService.getAllOwners());
    }

    @GetMapping("/get/vehiclesOwned")
    public ResponseEntity<List<VehicleDto>> getAllVehiclesOfOwner(@RequestParam(value = "id") Long id) {
        return ResponseEntity.ok(ownerService.getAllVehiclesOfOwner(id));
    }

    @PutMapping("/{id}/update")
    public ResponseEntity<ApiResponse> updateOwnerById(@PathVariable Long id, @RequestBody UserDto userDto) {
        return new ResponseEntity<>(ownerService.updateOwnerById(id, userDto), HttpStatusCode.valueOf(204));
    }

    @PutMapping("/update")
    public ResponseEntity<ApiResponse> updateOwnerByPhone(@RequestParam String phone, @RequestBody UserDto userDto) {
        return new ResponseEntity<>(ownerService.updateOwnerByPhone(phone, userDto), HttpStatusCode.valueOf(204));
    }
}
