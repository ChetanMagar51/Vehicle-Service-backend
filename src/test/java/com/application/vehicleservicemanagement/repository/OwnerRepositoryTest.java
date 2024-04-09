package com.application.vehicleservicemanagement.repository;

import com.application.vehicleservicemanagement.entity.Owner;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
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
public class OwnerRepositoryTest {

    @Autowired
    private OwnerRepository ownerRepository;

    @Autowired
    private TestEntityManager entityManager;

    @BeforeEach
    void setUp() {
        Owner owner=new Owner();
        owner.setEmail("johndoe@gmail.com");
        owner.setFirstName("John");
        owner.setLastName("Doe");
        owner.setPhone("9876543210");
        owner.setAddress("Pune, Maharashtra");
        entityManager.persistAndFlush(owner);
    }

    //Test case for success(Owner found by using email)
    @Test
    public void testFindByEmailFound(){
        Optional<Owner> foundOwnerOptional = ownerRepository.findByEmail("johndoe@gmail.com");
        assertTrue(foundOwnerOptional.isPresent());

        // Verify that the found owner matches the expected owner
        Owner foundOwner = foundOwnerOptional.get();
        assertEquals("John", foundOwner.getFirstName());
        assertEquals("Pune, Maharashtra", foundOwner.getAddress());

    }

    //Test case for failure(Owner not found by using email)
    @Test
    public void testFindByEmailNotFound(){
        Optional<Owner> foundOwnerOptional = ownerRepository.findByEmail("mathevfernandes@gmail.com");
        assertFalse(foundOwnerOptional.isPresent());
        assertThat(foundOwnerOptional.isEmpty()).isTrue();

    }

    //Test case for success(Owner found by using phone)
    @Test
    public void testFindByPhoneFound(){

        Optional<Owner> foundOwnerOptional = ownerRepository.findByPhone("9876543210");
        assertTrue(foundOwnerOptional.isPresent());

        // Verify that the found owner matches the expected owner
        Owner foundOwner = foundOwnerOptional.get();
        assertEquals("John", foundOwner.getFirstName());
        assertEquals("johndoe@gmail.com", foundOwner.getEmail());

    }

    //Test case for failure(Owner not found by using phone)
    @Test
    public void testFindByPhoneNotFound(){
        Optional<Owner> foundOwnerOptional = ownerRepository.findByEmail("1234567890");
        assertFalse(foundOwnerOptional.isPresent());
        assertThat(foundOwnerOptional.isEmpty()).isTrue();

    }

    @AfterEach
    void tearDown() {

        Optional<Owner> ownerOptional = ownerRepository.findByEmail("johndoe@gmail.com");
        ownerOptional.ifPresent(owner -> entityManager.remove(owner));
    }
}
