package com.application.vehicleservicemanagement.controller;

import com.application.vehicleservicemanagement.dto.ApiResponseDTO;
import com.application.vehicleservicemanagement.dto.VehicleDTO;
import com.application.vehicleservicemanagement.service.VehicleService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/vsm/vehicle")
@RequiredArgsConstructor
public class VehicleController {

    private final VehicleService vehicleService;

    @PostMapping("/register")
    public ResponseEntity<ApiResponseDTO> registerVehicle(@RequestBody VehicleDTO vehicleDTO) {
        return ResponseEntity.ok(vehicleService.registerVehicle(vehicleDTO));
    }

    @PostMapping("/get")
    public ResponseEntity<VehicleDTO> getVehicleByVehicleNumber(@RequestParam(value = "vehicleNumber") String vehicleNumber) {
        return ResponseEntity.ok(vehicleService.getVehicleByVehicleNumber(vehicleNumber));
    }

    @GetMapping("/get/all")
    public ResponseEntity<List<VehicleDTO>> getAllVehicles() {
        return ResponseEntity.ok(vehicleService.getAllVehicles());
    }

    @PostMapping("/schedule")
    public ResponseEntity<ApiResponseDTO> scheduleVehicleForService(@RequestParam(value = "vehicleNumber") String vehicleNumber, @RequestParam(value = "serviceAdvisorId") Long serviceAdvisorId) {
        return ResponseEntity.ok(vehicleService.scheduleVehicleForService(vehicleNumber, serviceAdvisorId));
    }

    @PostMapping("/startService")
    public ResponseEntity<ApiResponseDTO> startVehicleService(@RequestParam(value = "vehicleNumber") String vehicleNumber) {
        return ResponseEntity.ok(vehicleService.startVehicleService(vehicleNumber));
    }

    @PostMapping("/completeService")
    public ResponseEntity<ApiResponseDTO> completeVehicleService(@RequestParam(value = "vehicleNumber") String vehicleNumber) {
        return ResponseEntity.ok(vehicleService.completeVehicleService(vehicleNumber));
    }
}
