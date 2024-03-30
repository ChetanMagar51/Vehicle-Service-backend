package com.application.vehicleservicemanagement.repository;

import com.application.vehicleservicemanagement.entity.Item;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ItemRepository extends JpaRepository<Item, Long> {

    Optional<Item> findByNameIgnoreCase(String name);

    void deleteByName(String name);
}
