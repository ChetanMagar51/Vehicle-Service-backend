package com.application.vehicleservicemanagement.controller;

import com.application.vehicleservicemanagement.dto.ApiResponseDTO;
import com.application.vehicleservicemanagement.dto.ItemDTO;
import com.application.vehicleservicemanagement.service.VehicleService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/service")
@RequiredArgsConstructor
public class VehicleServiceStatusController {
    private final VehicleService vehicleService;

    @PostMapping("/schedule")
    public ResponseEntity<ApiResponseDTO> scheduleVehicleForService(@RequestParam(value = "vehicleNumber") String vehicleNumber, @RequestParam(value = "serviceAdvisorId") Long serviceAdvisorId) {
        return ResponseEntity.ok(vehicleService.scheduleVehicleForService(vehicleNumber, serviceAdvisorId));
    }

    @PostMapping("/startService")
    public ResponseEntity<ApiResponseDTO> startVehicleService(@RequestParam(value = "vehicleNumber") String vehicleNumber) {
        return ResponseEntity.ok(vehicleService.startVehicleService(vehicleNumber));
    }

    @PostMapping("/completeService")
    public ResponseEntity<ApiResponseDTO> completeVehicleService(@RequestParam(value = "vehicleNumber") String vehicleNumber, @RequestBody List<String> itemNameList) {
        return ResponseEntity.ok(vehicleService.completeVehicleService(vehicleNumber, itemNameList));
    }
}
