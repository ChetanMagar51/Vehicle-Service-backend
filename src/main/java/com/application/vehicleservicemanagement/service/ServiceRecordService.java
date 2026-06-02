package com.application.vehicleservicemanagement.service;

import java.util.List;

import com.application.vehicleservicemanagement.dto.ApiResponse;
import com.application.vehicleservicemanagement.dto.ServiceRecordDto;

public interface ServiceRecordService {

    List<ServiceRecordDto> getAllServiceRecords();

    ServiceRecordDto getServiceRecordById(Long id);

    List<ServiceRecordDto> getServiceRecordsByVehicle(Long vehicleId);
    
    
    List<ServiceRecordDto> getPaidServiceRecords();

    List<ServiceRecordDto> getUnpaidServiceRecords();
    
    
    ApiResponse approveServiceRecord(Long serviceRecordId);

    ApiResponse completePayment(Long serviceRecordId);

}