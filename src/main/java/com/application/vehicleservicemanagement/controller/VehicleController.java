package com.application.vehicleservicemanagement.controller;

import com.application.vehicleservicemanagement.dto.ApiResponse;
import com.application.vehicleservicemanagement.dto.VehicleDto;
import com.application.vehicleservicemanagement.dto.VehicleResponse;
import com.application.vehicleservicemanagement.entity.Vehicle;
import com.application.vehicleservicemanagement.service.VehicleService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/vehicle")
@RequiredArgsConstructor
public class VehicleController {

    private final VehicleService vehicleService;

    @PostMapping("/register")
    public ResponseEntity<ApiResponse> registerVehicle(@RequestBody VehicleDto vehicleDTO) {
        return new ResponseEntity<>(vehicleService.registerVehicle(vehicleDTO), HttpStatus.CREATED);
    }

    @GetMapping("/get")
    public ResponseEntity<VehicleResponse> getVehicleByVehicleNumber(@RequestParam(value = "vehicleNumber") String vehicleNumber) {
        return new ResponseEntity<>(vehicleService.getVehicleByVehicleNumber(vehicleNumber), HttpStatus.FOUND);
    }

    @GetMapping("/get/all")
    public ResponseEntity<List<VehicleResponse>> getAllVehicles() {
        return ResponseEntity.ok(vehicleService.getAllVehicles());
    }

    @GetMapping("/get/all/due")
    public ResponseEntity<List<VehicleResponse>> getAllDueVehicles() {
        return ResponseEntity.ok(vehicleService.getAllDueVehicles());
    }

    @GetMapping("/get/all/scheduled")
    public ResponseEntity<List<VehicleResponse>> getAllScheduledVehicles() {
        return ResponseEntity.ok(vehicleService.getAllScheduledVehicles());
    }

    @GetMapping("/get/all/underServicing")
    public ResponseEntity<List<VehicleResponse>> getAllVehiclesUnderServicing() {
        return ResponseEntity.ok(vehicleService.getAllVehiclesUnderServicing());
    }

    @GetMapping("/get/all/serviced")
    public ResponseEntity<List<VehicleResponse>> getAllServicedVehicles() {
        return ResponseEntity.ok(vehicleService.getAllServicedVehicles());
    }
}
