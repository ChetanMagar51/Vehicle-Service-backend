package com.application.vehicleservicemanagement.controller;

import com.application.vehicleservicemanagement.dto.ApiResponse;
import com.application.vehicleservicemanagement.dto.VehicleDto;
import com.application.vehicleservicemanagement.dto.VehicleResponse;
import com.application.vehicleservicemanagement.entity.ServiceStatus;
import com.application.vehicleservicemanagement.service.VehicleService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class VehicleControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private VehicleService vehicleService;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(new VehicleController(vehicleService))
                .build();
    }

    @Test
    void testRegisterVehicle() throws Exception{
        VehicleDto vehicleDto=new VehicleDto();
        vehicleDto.setId(1L);
        vehicleDto.setVehicleNumber("ABC123");

        when(vehicleService.registerVehicle(any(VehicleDto.class))).thenReturn(new ApiResponse("Vehicle registered successfully", "CREATED"));

        mockMvc.perform(MockMvcRequestBuilders.post("/vehicle/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"id\": 1, \"vehicleNumber\": \"ABC123\"}"))
                .andDo(print())
                .andExpect(status().isCreated());
    }

    @Test
    void getVehicleByVehicleNumber() throws Exception {
        VehicleResponse vehicle = new VehicleResponse();
        vehicle.setId(1L);
        vehicle.setVehicleNumber("XYZ456");

        when(vehicleService.getVehicleByVehicleNumber("XYZ456")).thenReturn(vehicle);

        mockMvc.perform(MockMvcRequestBuilders.get("/vehicle/get")
                        .param("vehicleNumber", "XYZ456")
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(MockMvcResultMatchers.status().isFound())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(1))
                .andExpect(MockMvcResultMatchers.jsonPath("$.vehicleNumber").value("XYZ456"));
    }

    @Test
    void getAllVehicles() throws Exception{
        VehicleResponse vehicle1=new VehicleResponse();
        vehicle1.setId(1L);
        vehicle1.setVehicleNumber("ABC123");

        VehicleResponse vehicle2=new VehicleResponse();
        vehicle2.setId(2L);
        vehicle2.setVehicleNumber("XYZ456");

        List<VehicleResponse> vehicleList = Arrays.asList(vehicle1, vehicle2);

        when(vehicleService.getAllVehicles()).thenReturn(vehicleList);

        mockMvc.perform(MockMvcRequestBuilders.get("/vehicle/all")
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].vehicleNumber").value("ABC123"))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].vehicleNumber").value("XYZ456"));
    }

    @Test
    void getAllDueVehicles() throws Exception{
        VehicleResponse vehicle1=new VehicleResponse();
        vehicle1.setServiceStatus(ServiceStatus.DUE);
        vehicle1.setVehicleNumber("ABC123");

        VehicleResponse vehicle2=new VehicleResponse();
        vehicle2.setServiceStatus(ServiceStatus.DUE);
        vehicle2.setVehicleNumber("XYZ456");

        List<VehicleResponse> dueVehicleList = Arrays.asList(vehicle1, vehicle2);

        when(vehicleService.getAllDueVehicles()).thenReturn(dueVehicleList);

        mockMvc.perform(MockMvcRequestBuilders.get("/vehicle/all/due")
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.length()").value(2))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].vehicleNumber").value("ABC123"))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].vehicleNumber").value("XYZ456"));
    }

    @Test
    void getAllScheduledVehicles() throws Exception {
        VehicleResponse vehicle1 = new VehicleResponse();
        vehicle1.setServiceStatus(ServiceStatus.SCHEDULED);
        vehicle1.setVehicleNumber("ABC123");

        VehicleResponse vehicle2 = new VehicleResponse();
        vehicle2.setServiceStatus(ServiceStatus.SCHEDULED);
        vehicle2.setVehicleNumber("XYZ456");

        List<VehicleResponse> scheduledVehicleList = Arrays.asList(vehicle1, vehicle2);

        when(vehicleService.getAllScheduledVehicles()).thenReturn(scheduledVehicleList);

        mockMvc.perform(MockMvcRequestBuilders.get("/vehicle/all/scheduled")
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.length()").value(2))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].vehicleNumber").value("ABC123"))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].vehicleNumber").value("XYZ456"));
    }

    @Test
    void getAllVehiclesUnderServicing() throws Exception {
        VehicleResponse vehicle1 = new VehicleResponse();
        vehicle1.setServiceStatus(ServiceStatus.UNDER_SERVICING);
        vehicle1.setVehicleNumber("ABC123");

        VehicleResponse vehicle2 = new VehicleResponse();
        vehicle2.setServiceStatus(ServiceStatus.UNDER_SERVICING);
        vehicle2.setVehicleNumber("XYZ456");

        List<VehicleResponse> underServicingVehicleList = Arrays.asList(vehicle1, vehicle2);

        when(vehicleService.getAllVehiclesUnderServicing()).thenReturn(underServicingVehicleList);

        mockMvc.perform(MockMvcRequestBuilders.get("/vehicle/all/underServicing")
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.length()").value(2))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].vehicleNumber").value("ABC123"))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].vehicleNumber").value("XYZ456"));
    }

    @Test
    void getAllServicedVehicles() throws Exception {
        VehicleResponse vehicle1 = new VehicleResponse();
        vehicle1.setServiceStatus(ServiceStatus.SERVICED);
        vehicle1.setVehicleNumber("ABC123");

        VehicleResponse vehicle2 = new VehicleResponse();
        vehicle2.setServiceStatus(ServiceStatus.SERVICED);
        vehicle2.setVehicleNumber("XYZ456");

        List<VehicleResponse> servicedVehicleList = Arrays.asList(vehicle1, vehicle2);

        when(vehicleService.getAllServicedVehicles()).thenReturn(servicedVehicleList);

        mockMvc.perform(MockMvcRequestBuilders.get("/vehicle/all/serviced")
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.length()").value(2))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].vehicleNumber").value("ABC123"))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].vehicleNumber").value("XYZ456"));
    }
}