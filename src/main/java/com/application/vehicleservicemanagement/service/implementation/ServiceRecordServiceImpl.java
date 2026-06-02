package com.application.vehicleservicemanagement.service.implementation;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.application.vehicleservicemanagement.dto.ApiResponse;
import com.application.vehicleservicemanagement.dto.ItemDetailDto;
import com.application.vehicleservicemanagement.dto.ServiceRecordDto;
import com.application.vehicleservicemanagement.entity.Item;
import com.application.vehicleservicemanagement.entity.ServiceRecord;
import com.application.vehicleservicemanagement.entity.Vehicle;
import com.application.vehicleservicemanagement.exception.ResourceNotFoundException;
import com.application.vehicleservicemanagement.repository.ServiceRecordRepository;
import com.application.vehicleservicemanagement.repository.VehicleRepository;
import com.application.vehicleservicemanagement.service.ServiceRecordService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ServiceRecordServiceImpl implements ServiceRecordService {

    private final ServiceRecordRepository serviceRecordRepository;
    private final VehicleRepository vehicleRepository;

    @Override
    public List<ServiceRecordDto> getAllServiceRecords() {

        return serviceRecordRepository.findAll()
                .stream()
                .map(this::convertToDto)
                .toList();
    }

    @Override
    public ServiceRecordDto getServiceRecordById(Long id) {

        ServiceRecord record =
                serviceRecordRepository.findById(id)
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Service Record",
                                        "id",
                                        id.toString()));

        return convertToDto(record);
    }

    @Override
    public List<ServiceRecordDto> getServiceRecordsByVehicle(Long vehicleId) {

        Vehicle vehicle =
                vehicleRepository.findById(vehicleId)
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Vehicle",
                                        "id",
                                        vehicleId.toString()));

        return serviceRecordRepository.findByVehicle(vehicle)
                .stream()
                .map(this::convertToDto)
                .toList();
    }
    
    
    @Override
    public List<ServiceRecordDto> getPaidServiceRecords() {

        return serviceRecordRepository
                .findByIsPaymentCompleted(true)
                .stream()
                .map(this::convertToDto)
                .toList();
    }

    @Override
    public List<ServiceRecordDto> getUnpaidServiceRecords() {

        return serviceRecordRepository
                .findByIsPaymentCompleted(false)
                .stream()
                .map(this::convertToDto)
                .toList();
    }
    
    
    
    
    
    
    
    
    
    @Override
    @Transactional
    public ApiResponse approveServiceRecord(Long serviceRecordId) {

        ServiceRecord record = serviceRecordRepository.findById(serviceRecordId)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Service Record",
                                "id",
                                serviceRecordId.toString()));

        if (Boolean.TRUE.equals(record.getIsAdminApproved())) {
            return ApiResponse.builder()
                    .status("Failed")
                    .message("Service record is already approved.")
                    .build();
        }

        record.setIsAdminApproved(true);

        serviceRecordRepository.save(record);

        return ApiResponse.builder()
                .status("Success")
                .message("Service record approved successfully.")
                .build();
    }
    
    
    @Override
    @Transactional
    public ApiResponse completePayment(Long serviceRecordId) {

        ServiceRecord record = serviceRecordRepository.findById(serviceRecordId)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Service Record",
                                "id",
                                serviceRecordId.toString()));

//        if (!Boolean.TRUE.equals(record.getIsAdminApproved())) {
//
//            return ApiResponse.builder()
//                    .status("Failed")
//                    .message("Service record must be approved before payment.")
//                    .build();
//        }

        if (Boolean.TRUE.equals(record.getIsPaymentCompleted())) {

            return ApiResponse.builder()
                    .status("Failed")
                    .message("Payment is already completed.")
                    .build();
        }

        record.setIsPaymentCompleted(true);

        serviceRecordRepository.save(record);

        return ApiResponse.builder()
                .status("Success")
                .message("Payment completed successfully.")
                .build();
    }
    
    
    
    
    
    
    
    

//    private ServiceRecordDto convertToDto(ServiceRecord record) {
//
//        Map<String, Integer> itemMap = new HashMap<>();
//
//        for (Map.Entry<Item, Integer> entry :
//                record.getItemQuantityMap().entrySet()) {
//
//            itemMap.put(
//                    entry.getKey().getName(),
//                    entry.getValue()
//            );
//        }
//
//        return ServiceRecordDto.builder()
//                .id(record.getId())
//                .serviceDate(record.getServiceDate())
//                .amount(record.getAmount())
//                .isAdminApproved(record.getIsAdminApproved())
//                .isPaymentCompleted(record.getIsPaymentCompleted())
//                .vehicleId(record.getVehicle().getId())
//                .vehicleNumber(record.getVehicle().getVehicleNumber())
//                .serviceAdvisorId(record.getServiceAdvisor().getId())
//                .serviceAdvisorName(
//                        record.getServiceAdvisor().getFirstName()
//                                + " "
//                                + record.getServiceAdvisor().getLastName()
//                )
//                .items(itemMap)
//                .build();
//    }
    
    
    private ServiceRecordDto convertToDto(ServiceRecord record) {

//        Map<String, Integer> itemMap = new HashMap<>();
//
//        for (Map.Entry<Item, Integer> entry :
//                record.getItemQuantityMap().entrySet()) {
//
//            itemMap.put(
//                    entry.getKey().getName(),
//                    entry.getValue()
//            );
//        }
    	
    	
    	Map<String, ItemDetailDto> itemMap = new HashMap<>();

    	for (Map.Entry<Item, Integer> entry :
    	        record.getItemQuantityMap().entrySet()) {

    	    Item item = entry.getKey();
    	    Integer qty = entry.getValue();

    	    double price = item.getPrice(); // assuming you have this field
    	    double total = price * qty;

    	    itemMap.put(
    	            item.getName(),
    	            ItemDetailDto.builder()
    	                    .quantity(qty)
    	                    .price(price)
    	                    .totalPrice(total)
    	                    .build()
    	    );
    	}

        return ServiceRecordDto.builder()
                .id(record.getId())
                .serviceDate(record.getServiceDate())
                .amount(record.getAmount())
//                .isAdminApproved(record.getIsAdminApproved())
                .isPaymentCompleted(record.getIsPaymentCompleted())

                // vehicle info
                .vehicleId(record.getVehicle().getId())
                .vehicleNumber(record.getVehicle().getVehicleNumber())

                // advisor info
                .serviceAdvisorId(record.getServiceAdvisor().getId())
                .serviceAdvisorName(
                        record.getServiceAdvisor().getFirstName()
                                + " "
                                + record.getServiceAdvisor().getLastName()
                )

                // ✅ OWNER INFO (NEW)
                .ownerId(record.getVehicle().getOwner().getId())
                .ownerName(
                        record.getVehicle().getOwner().getFirstName()
                                + " "
                                + record.getVehicle().getOwner().getLastName()
                )
                .ownerPhone(record.getVehicle().getOwner().getPhone())

                // items
                .items(itemMap)
                .build();
    }
}