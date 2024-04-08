package com.application.vehicleservicemanagement.dto;

import com.application.vehicleservicemanagement.entity.Owner;
import com.application.vehicleservicemanagement.entity.ServiceStatus;
import lombok.*;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class VehicleResponse {
    private Long id;
    private String vehicleNumber;
    private String vehicleModel;
    private String vehicleDescription;
    private Owner owner;
    private ServiceStatus serviceStatus;
}
