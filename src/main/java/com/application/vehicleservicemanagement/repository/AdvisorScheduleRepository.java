package com.application.vehicleservicemanagement.repository;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.application.vehicleservicemanagement.entity.AdvisorSchedule;
import com.application.vehicleservicemanagement.entity.ScheduleStatus;
import com.application.vehicleservicemanagement.entity.User;
import com.application.vehicleservicemanagement.entity.Vehicle;

public interface AdvisorScheduleRepository extends JpaRepository<AdvisorSchedule , Long> {
	
	
	List<AdvisorSchedule> findByServiceAdvisor(User advisor);

    List<AdvisorSchedule> findByServiceAdvisorAndScheduleDate(
            User advisor,
            LocalDate date);

    List<AdvisorSchedule> findByServiceAdvisorAndScheduleDateAndStatus(
            User advisor,
            LocalDate date,
            ScheduleStatus status);

    boolean existsByServiceAdvisorAndScheduleDateAndStartTime(
            User advisor,
            LocalDate date,
            java.time.LocalTime startTime);

	Optional<AdvisorSchedule> findByVehicle(Vehicle vehicle);
	
	

}
