package com.application.vehicleservicemanagement.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.application.vehicleservicemanagement.dto.ApiResponse;
import com.application.vehicleservicemanagement.dto.UserDto;
import com.application.vehicleservicemanagement.dto.VehicleDto;
import com.application.vehicleservicemanagement.exception.ResourceNotFoundException;
import com.application.vehicleservicemanagement.service.OwnerService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/owner")
@RequiredArgsConstructor
public class OwnerController {
    private final OwnerService ownerService;

    @PostMapping("/add")
    public ResponseEntity<ApiResponse> addOwner(@RequestBody UserDto userDto) {
    	
    	
    	ApiResponse response = ownerService.addOwner(userDto);

        if ("FAIL".equals(response.getStatus())) {
            return new ResponseEntity<>(response, HttpStatus.CONFLICT);
        }
    	
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @GetMapping("/get")
    public ResponseEntity<UserDto> getOwnerById(@RequestParam(value = "id") Long id) {
        return new ResponseEntity<>(ownerService.getOwnerById(id), HttpStatus.FOUND);
    }

    @GetMapping("/get/")
    public ResponseEntity<UserDto> getOwnerByPhone(@RequestBody String phone) {
        return new ResponseEntity<>(ownerService.getOwnerByPhone(phone), HttpStatus.FOUND);
    }

    @GetMapping("/get/all")
    public ResponseEntity<List<UserDto>> getAllOwners() {
        return ResponseEntity.ok(ownerService.getAllOwners());
    }

    @GetMapping("/get/vehiclesOwned")
    public ResponseEntity<List<VehicleDto>> getAllVehiclesOfOwner(@RequestParam(value = "id") Long id) {
        return ResponseEntity.ok(ownerService.getAllVehiclesOfOwner(id));
    }

    @PutMapping("/{id}/update")
    public ResponseEntity<ApiResponse> updateOwnerById(@PathVariable Long id, @RequestBody UserDto userDto) {
    	try {
            ApiResponse response = ownerService.updateOwnerById(id, userDto);
            return new ResponseEntity<>(response, HttpStatus.OK);

        } catch (ResourceNotFoundException ex) {
            return new ResponseEntity<>(
                    ApiResponse.builder()
                            .message(ex.getMessage())
                            .status("FAIL")
                            .build(),
                    HttpStatus.NOT_FOUND
            );

        } catch (RuntimeException ex) { // duplicate case
            return new ResponseEntity<>(
                    ApiResponse.builder()
                            .message(ex.getMessage())
                            .status("FAIL")
                            .build(),
                    HttpStatus.CONFLICT
            );}
    }

    @PutMapping("/update")
    public ResponseEntity<ApiResponse> updateOwnerByPhone(@RequestParam String phone, @RequestBody UserDto userDto) {
        return new ResponseEntity<>(ownerService.updateOwnerByPhone(phone, userDto), HttpStatus.OK);
    }

    @DeleteMapping("/delete")
    public ResponseEntity<ApiResponse> deleteOwnerById(@RequestParam(value = "id") Long id) {
        return new ResponseEntity<>(ownerService.deleteOwnerById(id), HttpStatus.OK);
    }
}
