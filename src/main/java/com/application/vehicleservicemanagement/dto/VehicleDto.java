package com.application.vehicleservicemanagement.dto;

import com.application.vehicleservicemanagement.entity.ServiceStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class VehicleDto {
    private Long id;
    private Long ownerId;
    private String vehicleNumber;
    private String vehicleModel;
    private String vehicleDescription;
    private ServiceStatus serviceStatus;
}
