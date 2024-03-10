package com.application.vehicleservicemanagement.repository;

import com.application.vehicleservicemanagement.entity.Vehicle;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface VehicleRepository extends JpaRepository<Vehicle, Long> {
    Optional<Vehicle> findByVehicleNumberIgnoreCase(String vehicleNumber);
}
