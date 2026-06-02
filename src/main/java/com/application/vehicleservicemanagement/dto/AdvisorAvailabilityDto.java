package com.application.vehicleservicemanagement.dto;

import java.time.LocalTime;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AdvisorAvailabilityDto {

    private Long id;

    private LocalTime workStartTime;

    private LocalTime workEndTime;

    private Integer slotDuration;
}