package com.application.vehicleservicemanagement.service;

import com.application.vehicleservicemanagement.dto.ApiResponse;
import com.application.vehicleservicemanagement.dto.ItemDto;

import java.util.List;

public interface ItemService {

    ApiResponse add(ItemDto itemDTO);

    ItemDto getById(Long itemId);

    ItemDto getByName(String name);

    List<ItemDto> getAllItems();

    ApiResponse updateById(Long itemId, ItemDto itemDTO);

    ApiResponse updateByName(String name, ItemDto itemDTO);

    ApiResponse deleteById(Long itemId);

    ApiResponse deleteByName(String name);
}
