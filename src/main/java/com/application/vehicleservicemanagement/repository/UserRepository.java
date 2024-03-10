package com.application.vehicleservicemanagement.repository;

import com.application.vehicleservicemanagement.entity.Role;
import com.application.vehicleservicemanagement.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);

    List<User> findAllByRole(Role role);
}
