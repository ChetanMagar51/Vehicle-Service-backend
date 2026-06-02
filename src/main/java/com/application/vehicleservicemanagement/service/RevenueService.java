package com.application.vehicleservicemanagement.service;

import com.application.vehicleservicemanagement.dto.RevenueReportDto;
import com.application.vehicleservicemanagement.dto.RevenueSummaryDto;

public interface RevenueService {

    RevenueSummaryDto getRevenueSummary();

    RevenueReportDto getTodayRevenue();
    
    RevenueReportDto getMonthlyRevenue();
    
    RevenueReportDto getYearlyRevenue();
}