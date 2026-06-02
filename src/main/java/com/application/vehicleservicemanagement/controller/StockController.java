package com.application.vehicleservicemanagement.controller;

import java.util.List;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.application.vehicleservicemanagement.dto.ApiResponse;
import com.application.vehicleservicemanagement.dto.ItemDto;
import com.application.vehicleservicemanagement.dto.StockMovementDto;
import com.application.vehicleservicemanagement.dto.StockRequestDto;
import com.application.vehicleservicemanagement.service.ItemService;
import com.application.vehicleservicemanagement.service.StockService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/stock")
@RequiredArgsConstructor
public class StockController {

    private final ItemService itemService;
    private final StockService stockService;

    // -------------------------
    // 📦 ITEM READ APIs
    // -------------------------
   
    
    @GetMapping("/all")
    public ResponseEntity<List<StockMovementDto>> getAllStockMovements() {
        return ResponseEntity.ok(stockService.getAllStockMovements());
    }
    
    
    @GetMapping("/items")
    public ResponseEntity<List<ItemDto>> getAllItems() {
        return ResponseEntity.ok(itemService.getAllItems());
    }

    @GetMapping("/items/out-of-stock")
    public ResponseEntity<List<ItemDto>> getOutOfStockItems() {
        return ResponseEntity.ok(itemService.getOutOfStockItems());
    }

    @GetMapping("/items/id")
    public ResponseEntity<ItemDto> getItemById(@RequestParam Long id) {
        return ResponseEntity.ok(itemService.getById(id));
    }

    @GetMapping("/items/name")
    public ResponseEntity<ItemDto> getItemByName(@RequestParam String name) {
        return ResponseEntity.ok(itemService.getByName(name));
    }

    // -------------------------
    // 🔥 STOCK OPERATIONS (NEW)
    // -------------------------

    @PostMapping("/in")
    public ResponseEntity<ApiResponse> stockIn(@RequestBody StockRequestDto request) {

        stockService.addStockIn(
                request.getItemId(),
                request.getQuantity(),
                request.getReason()
        );

        return ResponseEntity.ok(
                ApiResponse.builder()
                        .status("Success")
                        .message("Stock added successfully")
                        .build()
        );
    }

    @PostMapping("/out")
    public ResponseEntity<ApiResponse> stockOut(@RequestBody StockRequestDto request) {

        stockService.addStockOut(
                request.getItemId(),
                request.getQuantity(),
                request.getReason()
        );

        return ResponseEntity.ok(
                ApiResponse.builder()
                        .status("Success")
                        .message("Stock reduced successfully")
                        .build()
        );
    }
    
    
    @GetMapping("/history/{itemId}")
    public ResponseEntity<List<StockMovementDto>> getHistory(@PathVariable Long itemId) {
        return ResponseEntity.ok(stockService.getStockHistoryByItem(itemId));
    }
}