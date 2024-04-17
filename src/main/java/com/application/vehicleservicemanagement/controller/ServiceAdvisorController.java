package com.application.vehicleservicemanagement.controller;

import com.application.vehicleservicemanagement.service.VehicleService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;

@RestController
@RequestMapping("/serviceAdvisor")
@RequiredArgsConstructor
public class ServiceAdvisorController {
    private final VehicleService vehicleService;

    @GetMapping("/summary/{advisorId}")
    public ResponseEntity<HashMap<String, Integer>> getStatusSummaryForAdvisor(@PathVariable Long advisorId) {
        return ResponseEntity.ok(vehicleService.getStatusSummaryForAdvisor(advisorId));
    }
}
