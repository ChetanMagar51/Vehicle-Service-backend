package com.application.vehicleservicemanagement.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RevenueReportDto {

    private Double revenue;

    private Long totalServices;

    private Long paidServices;

    private Long unpaidServices;

    private Double averageRevenuePerService;

    private Double pendingAmount;
}