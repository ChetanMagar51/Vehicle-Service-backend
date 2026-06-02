package com.application.vehicleservicemanagement.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalTime;

@Entity
@Table(name = "AdvisorAvailability"	)
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AdvisorAvailability {
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
	
	@OneToOne
	@JoinColumn(name = "service_advisor_id")
	private User serviceAdvisor;

//    @ManyToOne
//    private User serviceAdvisor;

    private LocalTime workStartTime; // 09:00

    private LocalTime workEndTime;   // 18:00

    private Integer slotDuration;    // 60 minutes
}