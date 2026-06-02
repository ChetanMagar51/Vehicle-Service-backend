package com.application.vehicleservicemanagement.dto;

import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RevenueSummaryDto {

    private Double totalRevenue;

    private Double todayRevenue;

    private Double monthlyRevenue;

    private Long totalServices;

    private Long totalPaidServices;

    private Long totalUnpaidServices;
    

    private Double totalUnpaidAmount;
}