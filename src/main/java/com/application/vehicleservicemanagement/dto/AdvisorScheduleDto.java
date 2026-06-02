package com.application.vehicleservicemanagement.dto;

import com.application.vehicleservicemanagement.entity.ScheduleStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AdvisorScheduleDto {

    private Long id;

    private LocalDate scheduleDate;

    private LocalTime startTime;

    private LocalTime endTime;

    private ScheduleStatus status;

    private Integer estimatedDurationMinutes;

    private LocalDateTime actualStartTime;

    private LocalDateTime actualEndTime;

    private Integer delayMinutes;

    private String delayReason;

    private Boolean completedOnTime;

    private String remarks;

    private UserDto serviceAdvisor;

    private VehicleDto vehicle;
}