package com.application.vehicleservicemanagement.dto;

import java.time.LocalDateTime;
import java.util.Map;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ServiceRecordDto {

    private Long id;

    private LocalDateTime serviceDate;

    private Double amount;
//
//    private Boolean isAdminApproved;

    private Boolean isPaymentCompleted;

    private Long vehicleId;

    private String vehicleNumber;

    private Long serviceAdvisorId;

    private String serviceAdvisorName;
    
    private Long ownerId;

    private String ownerName;

    private String ownerPhone;

    
    private Map<String, ItemDetailDto> items;
    
//    private Map<String, Integer> items;
}