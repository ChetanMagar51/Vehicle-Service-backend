package com.application.vehicleservicemanagement.repository;

import com.application.vehicleservicemanagement.entity.Owner;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface OwnerRepository extends JpaRepository<Owner, Long> {
    Optional<Owner> findByEmail(String email);

    Optional<Owner> findByPhone(String phone);
}
