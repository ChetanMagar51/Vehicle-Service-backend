package com.application.vehicleservicemanagement.service.implementation;

import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import com.application.vehicleservicemanagement.dto.ApiResponse;
import com.application.vehicleservicemanagement.dto.ItemDto;
import com.application.vehicleservicemanagement.entity.Item;
import com.application.vehicleservicemanagement.exception.ResourceNotFoundException;
import com.application.vehicleservicemanagement.repository.ItemRepository;
import com.application.vehicleservicemanagement.service.ItemService;
import com.application.vehicleservicemanagement.service.StockService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ItemServiceImplementation implements ItemService {
    private final ItemRepository itemRepository;
    private final ModelMapper modelMapper;
    private final StockService stockService;

    @Override
    public ApiResponse add(ItemDto itemDTO) {
        Item item = Item.builder()
                .name(itemDTO.getName())
                .price(itemDTO.getPrice())
                .quantity(itemDTO.getQuantity())
                .build();
        itemRepository.save(item);
     // 🔥 IF initial stock is provided, use StockService
        if (itemDTO.getQuantity() != null && itemDTO.getQuantity() > 0) {
            stockService.addStockIn(
                    item.getId(),
                    itemDTO.getQuantity(),
                    "Initial stock entry"
            );
        }
        return ApiResponse.builder().message("Item added successfully.").status("Success").build();
    }

    @Override
    public ItemDto getById(Long itemId) {
        Item item = itemRepository.findById(itemId).orElseThrow(() -> new ResourceNotFoundException("Item", "itemId", itemId.toString()));
        return modelMapper.map(item, ItemDto.class);
    }

    @Override
    public ItemDto getByName(String name) {
        Item item = itemRepository.findByNameIgnoreCase(name).orElseThrow(() -> new ResourceNotFoundException("Item", "name", name));
        return modelMapper.map(item, ItemDto.class);
    }

    @Override
    public List<ItemDto> getAllItems() {
        List<Item> items = itemRepository.findAll();
        return items.stream().map(item -> modelMapper.map(item, ItemDto.class)).toList();
    }

    @Override
    public ApiResponse updateById(Long itemId, ItemDto itemDTO) {
        Item item = itemRepository.findById(itemId).orElseThrow(() -> new ResourceNotFoundException("Item", "itemId", itemId.toString()));
        item.setName(itemDTO.getName());
        item.setPrice(itemDTO.getPrice());
//        item.setQuantity(itemDTO.getQuantity());  // remove by the some essu
        itemRepository.save(item);
        return ApiResponse.builder().message("Item updated successfully.").status("Success").build();
    }

    @Override
    public ApiResponse updateByName(String name, ItemDto itemDTO) {
        Item item = itemRepository.findByNameIgnoreCase(name).orElseThrow(() -> new ResourceNotFoundException("Item", "name", name));
        item.setName(itemDTO.getName());
        item.setPrice(itemDTO.getPrice());
//        item.setQuantity(itemDTO.getQuantity());
        itemRepository.save(item);
        return ApiResponse.builder().message("Item updated successfully.").status("Success").build();
    }

    @Override
    public ApiResponse deleteById(Long itemId) {
        itemRepository.findById(itemId).orElseThrow(() -> new ResourceNotFoundException("Item", "itemId", itemId.toString()));
        itemRepository.deleteById(itemId);
        return ApiResponse.builder().message("Item deleted successfully.").status("Success").build();
    }

    @Override
    public ApiResponse deleteByName(String name) {
        itemRepository.findByNameIgnoreCase(name).orElseThrow(() -> new ResourceNotFoundException("Item", "name", name));
        itemRepository.deleteByName(name);
        return ApiResponse.builder().message("Item deleted successfully.").status("Success").build();
    }
    
    
    
    @Override
    public List<ItemDto> getOutOfStockItems() {
    	
    	List<Item> items = itemRepository.findByQuantityLessThanEqual(0);

        return 
                items.stream()
                .map(item -> modelMapper.map(item, ItemDto.class))
                .toList();
    }
    
    
    public ApiResponse updateStock(Long itemId, Integer quantity, String reason) {

        stockService.addStockIn(itemId, quantity, reason);

        return ApiResponse.builder()
                .message("Stock updated successfully.")
                .status("Success")
                .build();
    }
    
    public ApiResponse reduceStock(Long itemId, Integer quantity, String reason) {

        stockService.addStockOut(itemId, quantity, reason);

        return ApiResponse.builder()
                .message("Stock reduced successfully.")
                .status("Success")
                .build();
    }
}
