package com.application.vehicleservicemanagement.service.implementation;

import com.application.vehicleservicemanagement.dto.ApiResponse;
import com.application.vehicleservicemanagement.dto.ItemDto;
import com.application.vehicleservicemanagement.entity.Item;
import com.application.vehicleservicemanagement.repository.ItemRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.modelmapper.ModelMapper;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
public class ItemServiceImplementationTest {

    @Mock
    private ItemRepository itemRepository;

    @Mock
    private ModelMapper modelMapper;

    @InjectMocks
    private ItemServiceImplementation itemService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testAddItem() {
        ItemDto itemDto=new ItemDto();
        itemDto.setName("Oil");
        itemDto.setPrice(50.0);

        Item item=new Item();
        item.setName(itemDto.getName());
        item.setPrice(itemDto.getPrice());

        when(itemRepository.save(item)).thenReturn(item);
        when(modelMapper.map(ArgumentMatchers.any(ItemDto.class), eq(Item.class))).thenReturn(item);

        ApiResponse response = itemService.add(itemDto);

        assertNotNull(response);
        assertEquals("Item added successfully.", response.getMessage());
        assertEquals("Success", response.getStatus());
        verify(itemRepository, times(1)).save(item);
    }

    @Test
    public void testGetById() {
        Long itemId = 1L;
        Item item=new Item();

        when(itemRepository.findById(itemId)).thenReturn(Optional.of(item));
        when(modelMapper.map(item, ItemDto.class)).thenReturn(new ItemDto());

        ItemDto itemDto = itemService.getById(itemId);

        assertNotNull(itemDto);
        verify(itemRepository, times(1)).findById(itemId);

    }

    @Test
    public void testGetByName() {
        String itemName = "Oil";
        Item item = new Item();

        when(itemRepository.findByNameIgnoreCase(itemName)).thenReturn(Optional.of(item));
        when(modelMapper.map(item, ItemDto.class)).thenReturn(new ItemDto());

        ItemDto itemDto = itemService.getByName(itemName);

        assertNotNull(itemDto);
        verify(itemRepository, times(1)).findByNameIgnoreCase(itemName);
    }

    @Test
    public void testGetAllItems() {
        List<Item> items = new ArrayList<>();
        items.add(new Item());
        when(itemRepository.findAll()).thenReturn(items);
        when(modelMapper.map(ArgumentMatchers.any(Item.class), eq(ItemDto.class))).thenReturn(new ItemDto());

        List<ItemDto> itemDtos = itemService.getAllItems();

        assertNotNull(itemDtos);
        assertEquals(1, itemDtos.size());
        verify(itemRepository, times(1)).findAll();

    }

    @Test
    public void testUpdateById() {
        Long itemId = 1L;
        ItemDto itemDto=new ItemDto();
        itemDto.setName("Oil");
        itemDto.setPrice(50.0);

        Item item=new Item();
        item.setId(itemId);
        when(itemRepository.findById(itemId)).thenReturn(Optional.of(item));

        ApiResponse response = itemService.updateById(itemId,itemDto);

        assertNotNull(response);
        assertEquals("Item updated successfully.", response.getMessage());
        assertEquals("Success", response.getStatus());
        verify(itemRepository, times(1)).save(item);
    }

    @Test
    public void testUpdateByName() {
        String itemName = "Gear";
        ItemDto itemDto=new ItemDto();
        itemDto.setName("Gear");
        itemDto.setPrice(120.0);

        Item item=new Item();
        item.setName(itemName);
        when(itemRepository.findByNameIgnoreCase(itemName)).thenReturn(Optional.of(item));

        ApiResponse response = itemService.updateByName(itemName,itemDto);

        assertNotNull(response);
        assertEquals("Item updated successfully.", response.getMessage());
        assertEquals("Success", response.getStatus());
        verify(itemRepository, times(1)).save(item);
    }

    @Test
    public void testDeleteById() {
        Long itemId = 3L;
        Item itemToDelete = new Item();
        itemToDelete.setId(itemId);

        when(itemRepository.findById(itemId)).thenReturn(Optional.of(itemToDelete));

        ApiResponse response = itemService.deleteById(itemId);

        verify(itemRepository, times(1)).deleteById(itemId); // Use deleteById to verify deletion
        assertEquals("Item deleted successfully.", response.getMessage());
        assertEquals("Success", response.getStatus());
    }

    @Test
    public void testDeleteByName() {
        String itemName = "Brake";
        Item itemToDelete = new Item();
        itemToDelete.setName(itemName);

        when(itemRepository.findByNameIgnoreCase(itemName)).thenReturn(Optional.of(itemToDelete));

        ApiResponse response = itemService.deleteByName(itemName);

        verify(itemRepository, times(1)).deleteByName(itemName); // Use deleteById to verify deletion
        assertEquals("Item deleted successfully.", response.getMessage());
        assertEquals("Success", response.getStatus());

    }
}

