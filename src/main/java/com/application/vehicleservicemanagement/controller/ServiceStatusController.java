package com.application.vehicleservicemanagement.controller;

import com.application.vehicleservicemanagement.dto.ApiResponse;
import com.application.vehicleservicemanagement.service.VehicleService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;

@RestController
@RequestMapping("/service")
@RequiredArgsConstructor
public class ServiceStatusController {
    private final VehicleService vehicleService;

    @PostMapping("/schedule")
    public ResponseEntity<ApiResponse> scheduleVehicleForService(
            @RequestParam("vehicleNumber") String vehicleNumber,
            @RequestParam("serviceAdvisorId") Long serviceAdvisorId,
            @RequestParam("scheduleId") Long scheduleId) {

        return ResponseEntity.status(HttpStatus.ACCEPTED)
                .body(vehicleService.scheduleVehicleForService(
                        vehicleNumber,
                        serviceAdvisorId,
                        scheduleId));
    }

    @PostMapping("/startService")
    public ResponseEntity<ApiResponse> startVehicleService(@RequestParam(value = "vehicleNumber") String vehicleNumber) {
        return new ResponseEntity<>(vehicleService.startVehicleService(vehicleNumber), HttpStatus.ACCEPTED);
    }

    @PostMapping("/completeService")
    public ResponseEntity<ApiResponse> completeVehicleService(@RequestParam(value = "vehicleNumber") String vehicleNumber, @RequestBody HashMap<Long, Integer> itemQuantityMap) {
        return ResponseEntity.ok(vehicleService.completeVehicleService(vehicleNumber, itemQuantityMap));
    }
}
