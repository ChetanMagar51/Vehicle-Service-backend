package com.application.vehicleservicemanagement.controller;

import com.application.vehicleservicemanagement.dto.ApiResponseDTO;
import com.application.vehicleservicemanagement.dto.ItemDTO;
import com.application.vehicleservicemanagement.service.ItemService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/vsm/item")
@RequiredArgsConstructor
public class ItemController {
    private final ItemService itemService;

    public ResponseEntity<ApiResponseDTO> addItem(@RequestBody ItemDTO itemDTO) {
        return ResponseEntity.ok(itemService.add(itemDTO));
    }

    public ResponseEntity<ItemDTO> getItemById(@RequestParam(value = "id") Long id) {
        return ResponseEntity.ok(itemService.getById(id));
    }

    public ResponseEntity<ItemDTO> getItemByName(@RequestParam(value = "name") String name) {
        return ResponseEntity.ok(itemService.getByName(name));
    }

    public ResponseEntity<ApiResponseDTO> updateItemById(@RequestParam(value = "id") Long id, @RequestBody ItemDTO itemDTO) {
        return ResponseEntity.ok(itemService.updateById(id, itemDTO));
    }

    public ResponseEntity<ApiResponseDTO> updateItemByName(@RequestParam(value = "name") String name, @RequestBody ItemDTO itemDTO) {
        return ResponseEntity.ok(itemService.updateByName(name, itemDTO));
    }

    public ResponseEntity<ApiResponseDTO> deleteItemById(@RequestParam(value = "id") Long id) {
        return ResponseEntity.ok(itemService.deleteById(id));
    }

    public ResponseEntity<ApiResponseDTO> deleteItemByName(@RequestParam(value = "name") String name) {
        return ResponseEntity.ok(itemService.deleteByName(name));
    }
}
