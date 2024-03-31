package com.application.vehicleservicemanagement.controller;

import com.application.vehicleservicemanagement.dto.ApiResponseDTO;
import com.application.vehicleservicemanagement.dto.ItemDTO;
import com.application.vehicleservicemanagement.service.ItemService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/vsm/item")
@RequiredArgsConstructor
public class ItemController {
    private final ItemService itemService;

    @PostMapping("/add")
    public ResponseEntity<ApiResponseDTO> addItem(@RequestBody ItemDTO itemDTO) {
        return ResponseEntity.ok(itemService.add(itemDTO));
    }

    @GetMapping("/get/id")
    public ResponseEntity<ItemDTO> getItemById(@RequestParam(value = "id") Long id) {
        return ResponseEntity.ok(itemService.getById(id));
    }

    @GetMapping("/get/name")
    public ResponseEntity<ItemDTO> getItemByName(@RequestParam(value = "name") String name) {
        return ResponseEntity.ok(itemService.getByName(name));
    }

    @GetMapping("/get/all")
    public ResponseEntity<List<ItemDTO>> getAllItems() {
        return ResponseEntity.ok(itemService.getAllItems());
    }

    @PostMapping("/update/id")
    public ResponseEntity<ApiResponseDTO> updateItemById(@RequestParam(value = "id") Long id, @RequestBody ItemDTO itemDTO) {
        return ResponseEntity.ok(itemService.updateById(id, itemDTO));
    }

    @PostMapping("/update/name")
    public ResponseEntity<ApiResponseDTO> updateItemByName(@RequestParam(value = "name") String name, @RequestBody ItemDTO itemDTO) {
        return ResponseEntity.ok(itemService.updateByName(name, itemDTO));
    }

    @DeleteMapping("/delete/id")
    public ResponseEntity<ApiResponseDTO> deleteItemById(@RequestParam(value = "id") Long id) {
        return ResponseEntity.ok(itemService.deleteById(id));
    }

    @DeleteMapping("/delete/name")
    public ResponseEntity<ApiResponseDTO> deleteItemByName(@RequestParam(value = "name") String name) {
        return ResponseEntity.ok(itemService.deleteByName(name));
    }
}
