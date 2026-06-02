package com.application.vehicleservicemanagement.service;

import java.util.List;
import java.util.Map;

import com.application.vehicleservicemanagement.dto.ItemDto;
import com.application.vehicleservicemanagement.dto.StockMovementDto;

public interface StockService {
	// 🔥 Stock IN
    StockMovementDto addStockIn(Long itemId, Integer quantity, String reason);

    // 🔥 Stock OUT (includes validation)
    StockMovementDto addStockOut(Long itemId, Integer quantity, String reason);

    // 📊 NEW: stock history for item
    List<StockMovementDto> getStockHistoryByItem(Long itemId);

    // 📊 NEW: full stock history
    List<StockMovementDto> getAllStockMovements();
    
    Map<String, Long> getStockSummary();
    
    public List<ItemDto> getLowStockItems(Integer threshold) ;
}