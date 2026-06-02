package com.application.vehicleservicemanagement.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.application.vehicleservicemanagement.dto.RevenueReportDto;
import com.application.vehicleservicemanagement.dto.RevenueSummaryDto;
import com.application.vehicleservicemanagement.service.RevenueService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/revenue")
@RequiredArgsConstructor
public class RevenueController {

    private final RevenueService revenueService;

    @GetMapping("/summary")
    public ResponseEntity<RevenueSummaryDto> getRevenueSummary() {

        return ResponseEntity.ok(
                revenueService.getRevenueSummary()
        );
    }
    
    
   @GetMapping("/today")
    public ResponseEntity<RevenueReportDto> getTodayRevenue() {
        return ResponseEntity.ok(
                revenueService.getTodayRevenue());
    }

    @GetMapping("/month")
    public ResponseEntity<RevenueReportDto> getMonthlyRevenue() {
        return ResponseEntity.ok(
                revenueService.getMonthlyRevenue());
    }

    @GetMapping("/year")
    public ResponseEntity<RevenueReportDto> getYearlyRevenue() {
        return ResponseEntity.ok(
                revenueService.getYearlyRevenue());
    }
}