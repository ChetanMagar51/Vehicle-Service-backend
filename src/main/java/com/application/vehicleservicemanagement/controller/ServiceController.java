package com.application.vehicleservicemanagement.controller;

import com.application.vehicleservicemanagement.dto.ApiResponseDTO;
import com.application.vehicleservicemanagement.service.VehicleService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/vsm/service")
@RequiredArgsConstructor
public class ServiceController {
    private final VehicleService vehicleService;

    @PostMapping("/schedule")
    public ResponseEntity<ApiResponseDTO> scheduleVehicleForService(@RequestParam(value = "vehicleNumber", required = true) String vehicleNumber, @RequestParam(value = "serviceAdvisorId") Long serviceAdvisorId) {
        return ResponseEntity.ok(vehicleService.scheduleVehicleForService(vehicleNumber, serviceAdvisorId));
    }

    @PostMapping("/startService")
    public ResponseEntity<ApiResponseDTO> startVehicleService(@RequestParam(value = "vehicleNumber", required = true) String vehicleNumber) {
        return ResponseEntity.ok(vehicleService.startVehicleService(vehicleNumber));
    }

    @PostMapping("/completeService")
    public ResponseEntity<ApiResponseDTO> completeVehicleService(@RequestParam(value = "vehicleNumber", required = true) String vehicleNumber) {
        return ResponseEntity.ok(vehicleService.completeVehicleService(vehicleNumber));
    }
}
