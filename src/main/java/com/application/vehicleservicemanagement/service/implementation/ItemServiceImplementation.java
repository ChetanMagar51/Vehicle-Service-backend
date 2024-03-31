package com.application.vehicleservicemanagement.service.implementation;

import com.application.vehicleservicemanagement.dto.ApiResponseDTO;
import com.application.vehicleservicemanagement.dto.ItemDTO;
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
    public ApiResponseDTO add(ItemDTO itemDTO) {
        Item item = Item.builder()
                .name(itemDTO.getName())
                .price(itemDTO.getPrice())
                .build();
        itemRepository.save(item);
        return ApiResponseDTO.builder().message("Item added successfully.").status("Success").build();
    }

    @Override
    public ItemDTO getById(Long itemId) {
        Item item = itemRepository.findById(itemId).orElseThrow(() -> new ResourceNotFoundException("Item", "itemId", itemId.toString()));
        return modelMapper.map(item, ItemDTO.class);
    }

    @Override
    public ItemDTO getByName(String name) {
        Item item = itemRepository.findByNameIgnoreCase(name).orElseThrow(() -> new ResourceNotFoundException("Item", "name", name));
        return modelMapper.map(item, ItemDTO.class);
    }

    @Override
    public List<ItemDTO> getAllItems() {
        List<Item> items = itemRepository.findAll();
        return items.stream().map(item -> modelMapper.map(item, ItemDTO.class)).toList();
    }

    @Override
    public ApiResponseDTO updateById(Long itemId, ItemDTO itemDTO) {
        Item item = itemRepository.findById(itemId).orElseThrow(() -> new ResourceNotFoundException("Item", "itemId", itemId.toString()));
        item.setName(itemDTO.getName());
        item.setPrice(itemDTO.getPrice());
        itemRepository.save(item);
        return ApiResponseDTO.builder().message("Item updated successfully.").status("Success").build();
    }

    @Override
    public ApiResponseDTO updateByName(String name, ItemDTO itemDTO) {
        Item item = itemRepository.findByNameIgnoreCase(name).orElseThrow(() -> new ResourceNotFoundException("Item", "name", name));
        item.setName(itemDTO.getName());
        item.setPrice(itemDTO.getPrice());
        itemRepository.save(item);
        return ApiResponseDTO.builder().message("Item updated successfully.").status("Success").build();
    }

    @Override
    public ApiResponseDTO deleteById(Long itemId) {
        itemRepository.findById(itemId).orElseThrow(() -> new ResourceNotFoundException("Item", "itemId", itemId.toString()));
        itemRepository.deleteById(itemId);
        return ApiResponseDTO.builder().message("Item deleted successfully.").status("Success").build();
    }

    @Override
    public ApiResponseDTO deleteByName(String name) {
        itemRepository.findByNameIgnoreCase(name).orElseThrow(() -> new ResourceNotFoundException("Item", "name", name));
        itemRepository.deleteByName(name);
        return ApiResponseDTO.builder().message("Item deleted successfully.").status("Success").build();
    }
}
