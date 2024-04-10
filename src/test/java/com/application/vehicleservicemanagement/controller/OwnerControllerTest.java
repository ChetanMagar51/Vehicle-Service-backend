package com.application.vehicleservicemanagement.controller;

import com.application.vehicleservicemanagement.dto.ApiResponse;
import com.application.vehicleservicemanagement.dto.UserDto;
import com.application.vehicleservicemanagement.dto.VehicleDto;
import com.application.vehicleservicemanagement.service.OwnerService;
import com.application.vehicleservicemanagement.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class OwnerControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private OwnerService ownerService;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(new OwnerController(ownerService))
                .build();
    }

    @Test
    void addOwner() throws Exception {
        UserDto userDto = new UserDto();
        userDto.setId(1L);
        userDto.setFirstName("John");
        when(ownerService.addOwner(any(UserDto.class))).thenReturn(new ApiResponse("Owner added successfully", "CREATED"));

        mockMvc.perform(MockMvcRequestBuilders.post("/owner/add")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"id\": 1, \"firstName\": \"John\"}"))
                .andDo(print())
                .andExpect(status().isCreated());
    }

    @Test
    void getOwnerById() throws Exception {
        UserDto userDto = new UserDto();
        userDto.setId(1L);
        userDto.setFirstName("John");

        when(ownerService.getOwnerById(1L)).thenReturn(userDto);

        mockMvc.perform(MockMvcRequestBuilders.get("/owner/get")
                        .param("id", "1")
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(MockMvcResultMatchers.status().isFound())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(1))
                .andExpect(MockMvcResultMatchers.jsonPath("$.firstName").value("John"));
    }

    @Test
    void getOwnerByPhone() throws Exception {
        UserDto userDto = new UserDto();
        userDto.setId(1L);
        userDto.setPhone("9876543210");

        when(ownerService.getOwnerByPhone("9876543210")).thenReturn(userDto);

        mockMvc.perform(MockMvcRequestBuilders.get("/owner/get/")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("9876543210"))
                .andDo(print())
                .andExpect(MockMvcResultMatchers.status().isFound())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(1));
    }

    @Test
    void getAllOwners() throws Exception {
        UserDto owner1 = new UserDto();
        owner1.setId(1L);
        owner1.setFirstName("John");

        UserDto owner2 = new UserDto();
        owner2.setId(2L);
        owner2.setFirstName("Jane");

        List<UserDto> ownerList = Arrays.asList(owner1, owner2);
        when(ownerService.getAllOwners()).thenReturn(ownerList);

        mockMvc.perform(MockMvcRequestBuilders.get("/owner/get/all")
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].id").value(1))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].firstName").value("John"))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].id").value(2))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].firstName").value("Jane"));
    }

    @Test
    void getAllVehiclesOfOwner() throws Exception {
        VehicleDto vehicle1=new VehicleDto();
        vehicle1.setId(1L);
        vehicle1.setVehicleModel("Toyota");

        VehicleDto vehicle2=new VehicleDto();
        vehicle2.setId(2L);
        vehicle2.setVehicleModel("Honda");

        List<VehicleDto> vehicleList = Arrays.asList(vehicle1, vehicle2);
        when(ownerService.getAllVehiclesOfOwner(1L)).thenReturn(vehicleList);

        mockMvc.perform(MockMvcRequestBuilders.get("/owner/get/vehiclesOwned")
                        .param("id", "1")
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].id").value(1))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].id").value(2));
    }

    @Test
    void updateOwnerById() throws Exception {
        UserDto owner = new UserDto();
        owner.setId(1L);
        owner.setFirstName("Mathew");

        when(ownerService.updateOwnerById(eq(1L), any(UserDto.class))).thenReturn(new ApiResponse("Owner updated successfully","OK"));

        mockMvc.perform(MockMvcRequestBuilders.put("/owner/1/update")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"id\": 1, \"firstName\": \"UpdatedName\"}"))
                .andDo(print())
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    void updateOwnerByPhone() throws Exception {
        UserDto owner = new UserDto();
        owner.setId(1L);
        owner.setPhone("1234567890");

        when(ownerService.updateOwnerByPhone(eq("1234567890"), any(UserDto.class))).thenReturn(new ApiResponse("Owner updated successfully","OK"));

        mockMvc.perform(MockMvcRequestBuilders.put("/owner/update")
                        .param("phone", "1234567890")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"id\": 1, \"firstName\": \"UpdatedName\"}"))
                .andDo(print())
                .andExpect(MockMvcResultMatchers.status().isOk());
    }


}

