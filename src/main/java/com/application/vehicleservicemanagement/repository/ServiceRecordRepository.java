package com.application.vehicleservicemanagement.repository;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.application.vehicleservicemanagement.entity.ServiceRecord;
import com.application.vehicleservicemanagement.entity.Vehicle;

public interface ServiceRecordRepository extends JpaRepository<ServiceRecord , Long> {
	List<ServiceRecord> findByVehicle(Vehicle vehicle);
	List<ServiceRecord> findByIsPaymentCompleted(Boolean isPaymentCompleted);
	
	


List<ServiceRecord> findByIsPaymentCompletedAndServiceDateBetween(
        Boolean paymentCompleted,
        LocalDateTime start,
        LocalDateTime end);


List<ServiceRecord> findByServiceDateBetween(
        LocalDateTime startDate,
        LocalDateTime endDate);
}
