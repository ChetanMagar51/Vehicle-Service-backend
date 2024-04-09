package com.application.vehicleservicemanagement.repository;

import com.application.vehicleservicemanagement.entity.Item;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.EmbeddedDatabaseConnection;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@AutoConfigureTestDatabase(connection = EmbeddedDatabaseConnection.H2)
public class ItemRepositoryTest {

    @Autowired
    private ItemRepository itemRepository;

    @Autowired
    private TestEntityManager entityManager;

    @Test
    public void testFindByNameIgnoreCase() {
        // Create and persist an item
        Item item = new Item();
        item.setName("Oil");
        item.setDescription("Oil change");
        item.setPrice(150.0);
        entityManager.persistAndFlush(item);

        // Search for the item by name (case insensitive)
        Optional<Item> foundItemOptional = itemRepository.findByNameIgnoreCase("oil");

        // Verify that the found item is present and matches the expected item
        assertTrue(foundItemOptional.isPresent());
        assertThat(foundItemOptional.get().getName()).isEqualTo(item.getName());

    }

    @Test
    public void testDeleteByName() {
        // Create and persist an item
        Item item = new Item();
        item.setName("Brake pads");
        item.setDescription("Brake pads description");
        item.setPrice(100.0);
        entityManager.persistAndFlush(item);

        // Delete the item by name
        itemRepository.deleteByName("Brake pads");

        // Flush the changes to ensure the delete operation is executed
        entityManager.flush();

        // Verify that the item is deleted
        Optional<Item> deletedItemOptional = itemRepository.findByNameIgnoreCase("Brake pads");
        assertFalse(deletedItemOptional.isPresent());
    }

}
