package com.application.vehicleservicemanagement.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.application.vehicleservicemanagement.dto.ApiResponse;
import com.application.vehicleservicemanagement.dto.ServiceRecordDto;
import com.application.vehicleservicemanagement.service.ServiceRecordService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/service-record")
@RequiredArgsConstructor
public class ServiceRecordController {

    private final ServiceRecordService serviceRecordService;

    @GetMapping("/all")
    public ResponseEntity<List<ServiceRecordDto>>
    getAllServiceRecords() {

        return ResponseEntity.ok(
                serviceRecordService.getAllServiceRecords());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ServiceRecordDto>
    getServiceRecordById(@PathVariable Long id) {

        return ResponseEntity.ok(
                serviceRecordService.getServiceRecordById(id));
    }

    @GetMapping("/vehicle/{vehicleId}")
    public ResponseEntity<List<ServiceRecordDto>>
    getVehicleServiceHistory(
            @PathVariable Long vehicleId) {

        return ResponseEntity.ok(
                serviceRecordService
                        .getServiceRecordsByVehicle(vehicleId));
    }
    
    
    @GetMapping("/paid")
    public ResponseEntity<List<ServiceRecordDto>> getPaidServiceRecords() {

        return ResponseEntity.ok(
                serviceRecordService.getPaidServiceRecords()
        );
    }

    @GetMapping("/unpaid")
    public ResponseEntity<List<ServiceRecordDto>> getUnpaidServiceRecords() {

        return ResponseEntity.ok(
                serviceRecordService.getUnpaidServiceRecords()
        );
    }
    
    
    
    @PutMapping("/approve/{serviceRecordId}")
    public ResponseEntity<ApiResponse> approveServiceRecord(
            @PathVariable Long serviceRecordId) {

        return ResponseEntity.ok(
                serviceRecordService.approveServiceRecord(serviceRecordId)
        );
    }
    
    
    @PutMapping("/payment/complete/{serviceRecordId}")
    public ResponseEntity<ApiResponse> completePayment(
            @PathVariable Long serviceRecordId) {

        return ResponseEntity.ok(
                serviceRecordService.completePayment(serviceRecordId)
        );
    }
}