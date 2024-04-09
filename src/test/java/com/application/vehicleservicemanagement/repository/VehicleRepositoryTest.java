package com.application.vehicleservicemanagement.repository;

import com.application.vehicleservicemanagement.entity.ServiceStatus;
import com.application.vehicleservicemanagement.entity.Vehicle;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.EmbeddedDatabaseConnection;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@AutoConfigureTestDatabase(connection = EmbeddedDatabaseConnection.H2)
public class VehicleRepositoryTest {

    @Autowired
    private VehicleRepository vehicleRepository;

    @Autowired
    private TestEntityManager entityManager;

    @BeforeEach
    void setUp() {
        // Create vehicles with different service status
        Vehicle vehicle = new Vehicle();
        vehicle.setVehicleNumber("ABC123");
        vehicle.setServiceStatus(ServiceStatus.SERVICED); // Assuming ServiceStatus is an enum
        entityManager.persistAndFlush(vehicle);

        Vehicle vehicle1 = new Vehicle();
        vehicle1.setVehicleNumber("XYZ789");
        vehicle1.setServiceStatus(ServiceStatus.SCHEDULED);
        entityManager.persistAndFlush(vehicle1);

        Vehicle vehicle2 = new Vehicle();
        vehicle2.setVehicleNumber("DEF456");
        vehicle2.setServiceStatus(ServiceStatus.DUE);
        entityManager.persistAndFlush(vehicle2);

        Vehicle vehicle3 = new Vehicle();
        vehicle3.setVehicleNumber("GHI012");
        vehicle3.setServiceStatus(ServiceStatus.SCHEDULED);
        entityManager.persistAndFlush(vehicle3);
    }

    @Test
    public void testFindByVehicleNumberIgnoreCase() {

        // Search for the vehicle by vehicle number
        Optional<Vehicle> foundVehicleOptional = vehicleRepository.findByVehicleNumberIgnoreCase("abc123");
        assertTrue(foundVehicleOptional.isPresent());

        // Verify that the found vehicle matches the expected vehicle
        Vehicle foundVehicle = foundVehicleOptional.get();
        assertEquals(ServiceStatus.SERVICED, foundVehicle.getServiceStatus());
        assertEquals("ABC123", foundVehicle.getVehicleNumber());
    }

    @Test
    public void testFindAllByServiceStatus() {
        // Search for vehicles by service status

        List<Vehicle> vehiclesInService = vehicleRepository.findAllByServiceStatus(ServiceStatus.SCHEDULED);
        assertEquals(2, vehiclesInService.size());

        List<Vehicle> vehiclesUnderServicing = vehicleRepository.findAllByServiceStatus(ServiceStatus.UNDER_SERVICING);
        assertEquals(0, vehiclesUnderServicing.size());
        assertFalse(vehiclesUnderServicing.stream().anyMatch(v -> v.getVehicleNumber().equals("GHI012")));

        List<Vehicle> vehiclesDue = vehicleRepository.findAllByServiceStatus(ServiceStatus.DUE);
        assertEquals(1, vehiclesDue.size());
        assertTrue(vehiclesDue.stream().anyMatch(v -> v.getVehicleNumber().equals("DEF456")));
    }

    @AfterEach
    void tearDown() {

        vehicleRepository.deleteAll();
    }
}