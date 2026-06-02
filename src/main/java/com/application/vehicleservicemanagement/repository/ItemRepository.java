package com.application.vehicleservicemanagement.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.application.vehicleservicemanagement.entity.Item;

public interface ItemRepository extends JpaRepository<Item, Long> {

    Optional<Item> findByNameIgnoreCase(String name);

    void deleteByName(String name);
    
    
//    List<Item> findByQuantity(Integer quantity);
    
    List<Item> findByQuantityLessThanEqual(Integer quantity);
    
}
