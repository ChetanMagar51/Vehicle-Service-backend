package com.application.vehicleservicemanagement.repository;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.JpaRepository;

import com.application.vehicleservicemanagement.entity.Item;
import com.application.vehicleservicemanagement.entity.StockMovement;

public interface StockMovementRepository extends JpaRepository<StockMovement, Long> {
	
	List<StockMovement> findByItemIdOrderByDateDesc(Long itemId);
	
	List<Item> findByQuantityLessThanEqual(Integer threshold);
	
	
	@Query("SELECT sm.type, SUM(sm.quantity) FROM StockMovement sm GROUP BY sm.type")
    List<Object[]> getStockSummary();

}
