package com.application.vehicleservicemanagement.service.implementation;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.application.vehicleservicemanagement.dto.ItemDto;
import com.application.vehicleservicemanagement.dto.StockMovementDto;
import com.application.vehicleservicemanagement.entity.Item;
import com.application.vehicleservicemanagement.entity.MovementType;
import com.application.vehicleservicemanagement.entity.StockMovement;
import com.application.vehicleservicemanagement.repository.ItemRepository;
import com.application.vehicleservicemanagement.repository.StockMovementRepository;
import com.application.vehicleservicemanagement.service.StockService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class StockServiceImpl implements StockService {
	

    private final ModelMapper modelMapper;

    private final ItemRepository itemRepository;
    private final StockMovementRepository stockMovementRepository;

    @Override
    @Transactional
    public StockMovementDto addStockIn(Long itemId, Integer quantity, String reason) {

        Item item = itemRepository.findById(itemId)
                .orElseThrow(() -> new RuntimeException("Item not found"));

        // 1. update stock
        item.setQuantity(item.getQuantity() + quantity);
        itemRepository.save(item);

        // 2. create movement
        StockMovement movement = StockMovement.builder()
                .item(item)
                .type(MovementType.IN)
                .quantity(quantity)
                .reason(reason)
                .date(LocalDate.now())
                .createdAt(LocalDateTime.now())
                .unitPriceAtThatTime(item.getPrice())
                .build();

        StockMovement saved = stockMovementRepository.save(movement);

        return mapToDto(saved);
    }

    @Override
    @Transactional
    public StockMovementDto addStockOut(Long itemId, Integer quantity, String reason) {

        Item item = itemRepository.findById(itemId)
                .orElseThrow(() -> new RuntimeException("Item not found"));

        // 🔥 VALIDATION
        if (item.getQuantity() < quantity) {
            throw new RuntimeException("Insufficient stock available");
        }

        // 1. reduce stock
        item.setQuantity(item.getQuantity() - quantity);
        itemRepository.save(item);

        // 2. create movement
        StockMovement movement = StockMovement.builder()
                .item(item)
                .type(MovementType.OUT)
                .quantity(quantity)
                .reason(reason)
                .date(LocalDate.now())
                .createdAt(LocalDateTime.now())
                .unitPriceAtThatTime(item.getPrice())
                .build();

        StockMovement saved = stockMovementRepository.save(movement);

        return mapToDto(saved);
    }

    // -------------------------
    // DTO MAPPING
    // -------------------------
    private StockMovementDto mapToDto(StockMovement movement) {

        return StockMovementDto.builder()
                .id(movement.getId())
                .itemId(movement.getItem().getId())
                .itemName(movement.getItem().getName())
                .type(movement.getType().name())
                .quantity(movement.getQuantity())
                .reason(movement.getReason())
                .date(movement.getDate())
                .createdAt(movement.getCreatedAt())
                .unitPriceAtThatTime(movement.getUnitPriceAtThatTime())
                .build();
    }

	@Override
	public List<StockMovementDto> getStockHistoryByItem(Long itemId) {

	    List<StockMovement> list =
	            stockMovementRepository.findByItemIdOrderByDateDesc(itemId);

	    return list.stream()
	            .map(this::mapToDto)
	            .toList();
	}

	
	@Override
	public List<StockMovementDto> getAllStockMovements() {

	    List<StockMovement> movements = stockMovementRepository.findAll();

	    return movements.stream()
	            .map(this::mapToDto)
	            .toList();
	}
	
	@Override
	public List<ItemDto> getLowStockItems(Integer threshold) {

	    List<Item> items = itemRepository.findByQuantityLessThanEqual(threshold);

	    return items.stream().map(item -> modelMapper.map(item, ItemDto.class))
	            .toList();
	}
	
	
	@Override
	public Map<String, Long> getStockSummary() {
	    return stockMovementRepository.getStockSummary()
	            .stream()
	            .collect(Collectors.toMap(
	                row -> String.valueOf(row[0]),   // MovementType enum -> "IN" / "OUT"
	                row -> row[1] == null ? 0L : ((Number) row[1]).longValue()
	            ));
	}
	
	@Transactional
	public void rollbackStock(Long itemId, Integer quantity, String reason) {

	    Item item = itemRepository.findById(itemId)
	            .orElseThrow(() -> new RuntimeException("Item not found"));

	    // reverse stock
	    item.setQuantity(item.getQuantity() + quantity);
	    itemRepository.save(item);

	    StockMovement movement = StockMovement.builder()
	            .item(item)
	            .type(MovementType.IN)
	            .quantity(quantity)
	            .reason("ROLLBACK: " + reason)
	            .date(LocalDate.now())
	            .createdAt(LocalDateTime.now())
	            .unitPriceAtThatTime(item.getPrice())
	            .build();

	    stockMovementRepository.save(movement);
	}
}