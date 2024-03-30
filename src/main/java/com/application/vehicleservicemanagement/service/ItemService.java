package com.application.vehicleservicemanagement.service;

import com.application.vehicleservicemanagement.dto.ApiResponseDTO;
import com.application.vehicleservicemanagement.dto.ItemDTO;

public interface ItemService {

    ApiResponseDTO add(ItemDTO itemDTO);

    ItemDTO getById(Long itemId);

    ItemDTO getByName(String name);

    ApiResponseDTO updateById(Long itemId, ItemDTO itemDTO);

    ApiResponseDTO updateByName(String name, ItemDTO itemDTO);

    ApiResponseDTO deleteById(Long itemId);

    ApiResponseDTO deleteByName(String name);
}
