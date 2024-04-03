package com.application.vehicleservicemanagement.service.implementation;

import com.application.vehicleservicemanagement.dto.ApiResponse;
import com.application.vehicleservicemanagement.dto.ItemDto;
import com.application.vehicleservicemanagement.entity.Item;
import com.application.vehicleservicemanagement.exception.ResourceNotFoundException;
import com.application.vehicleservicemanagement.repository.ItemRepository;
import com.application.vehicleservicemanagement.service.ItemService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ItemServiceImplementation implements ItemService {
    private final ItemRepository itemRepository;
    private final ModelMapper modelMapper;

    @Override
    public ApiResponse add(ItemDto itemDTO) {
        Item item = Item.builder()
                .name(itemDTO.getName())
                .price(itemDTO.getPrice())
                .build();
        itemRepository.save(item);
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
        itemRepository.save(item);
        return ApiResponse.builder().message("Item updated successfully.").status("Success").build();
    }

    @Override
    public ApiResponse updateByName(String name, ItemDto itemDTO) {
        Item item = itemRepository.findByNameIgnoreCase(name).orElseThrow(() -> new ResourceNotFoundException("Item", "name", name));
        item.setName(itemDTO.getName());
        item.setPrice(itemDTO.getPrice());
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
}
