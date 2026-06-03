package com.application.vehicleservicemanagement.controller;

import com.application.vehicleservicemanagement.dto.ApiResponse;
import com.application.vehicleservicemanagement.dto.ItemDto;
import com.application.vehicleservicemanagement.service.ItemService;
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
class ItemControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ItemService itemService;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(new ItemController(itemService))
                .build();
    }

    @Test
    void testAddItem() throws Exception{
        ItemDto itemDto = new ItemDto();
        itemDto.setId(1L);
        itemDto.setName("Oil");

        when(itemService.add(any(ItemDto.class))).thenReturn(new ApiResponse("Item added successfully", "CREATED", itemDto));

        mockMvc.perform(MockMvcRequestBuilders.post("/item/add")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"id\": 1, \"name\": \"Oil\"}"))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("Item added successfully"));

    }

    @Test
    void testGetItemById() throws Exception{
        ItemDto itemDto = new ItemDto();
        itemDto.setId(1L);
        itemDto.setName("Brake");

        when(itemService.getById(1L)).thenReturn(itemDto);

        mockMvc.perform(MockMvcRequestBuilders.get("/item/get/id")
                        .param("id", "1")
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isFound())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(1));
    }

    @Test
    void testGetItemByName() throws Exception{
        ItemDto itemDto = new ItemDto();
        itemDto.setId(1L);
        itemDto.setName("Brake");

        when(itemService.getByName("Brake")).thenReturn(itemDto);

        mockMvc.perform(MockMvcRequestBuilders.get("/item/get/name")
                        .param("name", "Brake")
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isFound())
                .andExpect(MockMvcResultMatchers.jsonPath("$.name").value("Brake"));
    }

    @Test
    void testGetAllItems() throws Exception{
        ItemDto item1 = new ItemDto();
        item1.setId(1L);
        item1.setName("Oil");

        ItemDto item2 = new ItemDto();
        item2.setId(2L);
        item2.setName("Brake");

        List<ItemDto> itemList = Arrays.asList(item1,item2);

        when(itemService.getAllItems()).thenReturn(itemList);

        mockMvc.perform(MockMvcRequestBuilders.get("/item/get/all")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].name").value("Oil"))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].name").value("Brake"));
    }

    @Test
    void testUpdateItemById() throws Exception {
        ItemDto itemDto = new ItemDto();
        itemDto.setId(1L);
        itemDto.setName("Engine");

        when(itemService.updateById(1L, itemDto)).thenReturn(new ApiResponse("Item updated successfully", "OK", itemDto));

        mockMvc.perform(MockMvcRequestBuilders.put("/item/update/id")
                        .param("id", "1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"id\": 1, \"name\": \"Engine\"}"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("Item updated successfully"));
    }

    @Test
    void testUpdateItemByName() throws Exception {
        ItemDto itemDto = new ItemDto();
        itemDto.setId(1L);
        itemDto.setName("Gearbox");

        when(itemService.updateByName("Steering", itemDto)).thenReturn(new ApiResponse("Item updated successfully", "OK", itemDto));

        mockMvc.perform(MockMvcRequestBuilders.put("/item/update/name")
                        .param("name", "Steering") // Request with the expected name
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"id\": 1, \"name\": \"Gearbox\"}"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("Item updated successfully"));
    }

    @Test
    void testDeleteItemById() throws Exception {
        when(itemService.deleteById(1L)).thenReturn(new ApiResponse("Item deleted successfully", "OK", mockMvc));

        mockMvc.perform(MockMvcRequestBuilders.delete("/item/delete/id")
                        .param("id", "1"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("Item deleted successfully"));
    }

    @Test
    void testDeleteItemByName() throws Exception {
        when(itemService.deleteByName("Clutch")).thenReturn(new ApiResponse("Item deleted successfully", "OK", mockMvc));

        mockMvc.perform(MockMvcRequestBuilders.delete("/item/delete/name")
                        .param("name", "Clutch"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("Item deleted successfully"));
    }
}