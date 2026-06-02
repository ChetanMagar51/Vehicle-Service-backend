package com.application.vehicleservicemanagement.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.application.vehicleservicemanagement.entity.AdvisorAvailability;
import com.application.vehicleservicemanagement.entity.User;

public interface AdvisorAvailabilityRepository extends JpaRepository<AdvisorAvailability, Long> {

	Optional<AdvisorAvailability> findByServiceAdvisor(User advisor);
	

}
