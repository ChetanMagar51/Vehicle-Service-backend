package com.application.vehicleservicemanagement.controller;

import com.application.vehicleservicemanagement.dto.ApiResponse;
import com.application.vehicleservicemanagement.dto.ItemDto;
import com.application.vehicleservicemanagement.service.ItemService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/item")
@RequiredArgsConstructor
public class ItemController {
    private final ItemService itemService;

    @PostMapping("/add")
    public ResponseEntity<ApiResponse> addItem(@RequestBody ItemDto itemDTO) {
        return new ResponseEntity<>(itemService.add(itemDTO), HttpStatus.CREATED);
    }

    @GetMapping("/get/id")
    public ResponseEntity<ItemDto> getItemById(@RequestParam(value = "id") Long id) {
        return new ResponseEntity<>(itemService.getById(id), HttpStatus.FOUND);
    }

    @GetMapping("/get/name")
    public ResponseEntity<ItemDto> getItemByName(@RequestParam(value = "name") String name) {
        return new ResponseEntity<>(itemService.getByName(name), HttpStatus.FOUND);
    }

    @GetMapping("/get/all")
    public ResponseEntity<List<ItemDto>> getAllItems() {
        return ResponseEntity.ok(itemService.getAllItems());
    }

    @PutMapping("/update/id")
    public ResponseEntity<ApiResponse> updateItemById(@RequestParam(value = "id") Long id, @RequestBody ItemDto itemDTO) {
        return ResponseEntity.ok(itemService.updateById(id, itemDTO));
    }

    @PutMapping("/update/name")
    public ResponseEntity<ApiResponse> updateItemByName(@RequestParam(value = "name") String name, @RequestBody ItemDto itemDTO) {
        return ResponseEntity.ok(itemService.updateByName(name, itemDTO));
    }

    @DeleteMapping("/delete/id")
    public ResponseEntity<ApiResponse> deleteItemById(@RequestParam(value = "id") Long id) {
        return ResponseEntity.ok(itemService.deleteById(id));
    }

    @DeleteMapping("/delete/name")
    public ResponseEntity<ApiResponse> deleteItemByName(@RequestParam(value = "name") String name) {
        return ResponseEntity.ok(itemService.deleteByName(name));
    }
}
