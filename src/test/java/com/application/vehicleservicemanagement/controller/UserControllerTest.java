package com.application.vehicleservicemanagement.controller;

import com.application.vehicleservicemanagement.dto.ApiResponse;
import com.application.vehicleservicemanagement.dto.UserDto;
import com.application.vehicleservicemanagement.service.UserService;
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
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class UserControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService userService;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(new UserController(userService))
                .build();
    }

    @Test
    void testGetUser() throws Exception {
        UserDto userDto=new UserDto();
        userDto.setId(1L);
        userDto.setFirstName("John");
        when(userService.getUserById(1L)).thenReturn(userDto);

        mockMvc.perform(MockMvcRequestBuilders.get("/user/get")
                        .param("id", "1")
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isFound())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(1));
    }
    @Test
    void testGetAllUsers() throws Exception {
        UserDto user1=new UserDto();
        user1.setId(1L);
        user1.setFirstName("John");

        UserDto user2=new UserDto();
        user2.setId(2L);
        user2.setFirstName("Mathew");

        List<UserDto> userList = Arrays.asList(user1, user2);
        when(userService.getAllUsers()).thenReturn(userList);

        mockMvc.perform(MockMvcRequestBuilders.get("/user/get/all")
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].id").value(1))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].firstName").value("John"))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].id").value(2))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].firstName").value("Mathew"));
    }

    @Test
    void testGetAllServiceAdvisors() throws Exception {
        UserDto advisor1=new UserDto();
        advisor1.setId(1L);
        advisor1.setFirstName("Advisor1");

        UserDto advisor2=new UserDto();
        advisor2.setId(2L);
        advisor2.setFirstName("Advisor2");

        List<UserDto> advisorList = Arrays.asList(advisor1, advisor2);
        when(userService.getAllServiceAdvisors()).thenReturn(advisorList);

        mockMvc.perform(MockMvcRequestBuilders.get("/user/get/all/serviceAdvisor")
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].id").value(1))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].firstName").value("Advisor1"))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].id").value(2))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].firstName").value("Advisor2"));
    }

    @Test
    void updateUserById() throws Exception {
        UserDto updatedUserDto = new UserDto();
        updatedUserDto.setId(1L);
        updatedUserDto.setFirstName("John");

        when(userService.updateUserById(eq(1L), any(UserDto.class))).thenReturn(new ApiResponse("User updated successfully","OK"));

        mockMvc.perform(MockMvcRequestBuilders.patch("/user/update")
                        .param("id", "1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"id\": 1, \"firstName\": \"UpdatedName\"}"))
                .andExpect(status().isOk());
    }

    @Test
    void deleteUser() throws Exception {
        when(userService.deleteUserById(1L)).thenReturn(new ApiResponse("User deleted successfully","OK"));

        mockMvc.perform(MockMvcRequestBuilders.delete("/user/delete")
                        .param("id", "1"))
                .andDo(print())
                .andExpect(status().isOk());
    }
}