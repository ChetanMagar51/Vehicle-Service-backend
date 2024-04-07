package com.application.vehicleservicemanagement.controller;

import com.application.vehicleservicemanagement.dto.ApiResponse;
import com.application.vehicleservicemanagement.dto.VehicleDto;
import com.application.vehicleservicemanagement.service.VehicleService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/vehicle")
@RequiredArgsConstructor
public class VehicleController {

    private final VehicleService vehicleService;

    @PostMapping("/register")
    public ResponseEntity<ApiResponse> registerVehicle(@RequestParam(value = "ownerId") Long ownerId, @RequestBody VehicleDto vehicleDTO) {
        return ResponseEntity.ok(vehicleService.registerVehicle(ownerId, vehicleDTO));
    }

    @PostMapping("/get")
    public ResponseEntity<VehicleDto> getVehicleByVehicleNumber(@RequestParam(value = "vehicleNumber") String vehicleNumber) {
        return ResponseEntity.ok(vehicleService.getVehicleByVehicleNumber(vehicleNumber));
    }

    @GetMapping("/get/all")
    public ResponseEntity<List<VehicleDto>> getAllVehicles() {
        return ResponseEntity.ok(vehicleService.getAllVehicles());
    }

    @GetMapping("/get/all/due")
    public ResponseEntity<List<VehicleDto>> getAllDueVehicles() {
        return ResponseEntity.ok(vehicleService.getAllDueVehicles());
    }

    @GetMapping("/get/all/scheduled")
    public ResponseEntity<List<VehicleDto>> getAllScheduledVehicles() {
        return ResponseEntity.ok(vehicleService.getAllScheduledVehicles());
    }

    @GetMapping("/get/all/underServicing")
    public ResponseEntity<List<VehicleDto>> getAllVehiclesUnderServicing() {
        return ResponseEntity.ok(vehicleService.getAllVehiclesUnderServicing());
    }

    @GetMapping("/get/all/serviced")
    public ResponseEntity<List<VehicleDto>> getAllServicedVehicles() {
        return ResponseEntity.ok(vehicleService.getAllServicedVehicles());
    }
}
