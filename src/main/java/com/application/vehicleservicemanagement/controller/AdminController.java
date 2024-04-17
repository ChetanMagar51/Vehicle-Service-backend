package com.application.vehicleservicemanagement.controller;

import com.application.vehicleservicemanagement.service.VehicleService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;

@RestController
@RequestMapping("/admin")
@RequiredArgsConstructor
public class AdminController {
    private final VehicleService vehicleService;

    @GetMapping("/summary")
    public ResponseEntity<HashMap<String, Integer>> getStatusSummary() {
        return ResponseEntity.ok(vehicleService.getStatusSummary());
    }
}
