package com.application.vehicleservicemanagement.service;

import java.util.ArrayList;
import java.util.List;

import com.application.vehicleservicemanagement.dto.AdminRegisterRequest;
import com.application.vehicleservicemanagement.dto.ApiResponse;
import com.application.vehicleservicemanagement.dto.RegisterRequest;
import com.application.vehicleservicemanagement.dto.ServiceAdvisorDto;
import com.application.vehicleservicemanagement.dto.UserDto;

public interface UserService {
    ApiResponse createUser(RegisterRequest registerRequest);

    UserDto getUserById(Long id);

    List<UserDto> getAllUsers();
    
//    default List<ServiceAdvisorDto> getAllServiceAdvisorsWthiTime()	
//    {
//    	List<ServiceAdvisorDto> li = new ArrayList();
//    	return li;
//    }
    


    List<ServiceAdvisorDto> getAllServiceAdvisors();

    ApiResponse updateUserById(Long id, UserDto userDTO);

    ApiResponse deleteUserById(Long id);
    
    
    ApiResponse createUserAdmin(AdminRegisterRequest registerRequest);
}
