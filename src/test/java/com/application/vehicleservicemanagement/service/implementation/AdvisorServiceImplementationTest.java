package com.application.vehicleservicemanagement.service.implementation;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import com.application.vehicleservicemanagement.entity.User;
import com.application.vehicleservicemanagement.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class AdvisorServiceImplementationTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private AdvisorServiceImplementation advisorService;

    @Test
    public void testUpdateAdvisorStatus() {
        User serviceAdvisor = new User();
        // The advisor can schedule 2 vehicles
        serviceAdvisor.setScheduledVehicleCount(2);
        serviceAdvisor.setIsAvailable(true);

        // Mocking the repository save method to return the same advisor object
        when(userRepository.save(any(User.class))).thenAnswer(invocation -> invocation.getArgument(0));

        boolean updated = advisorService.updateAdvisorStatus(serviceAdvisor);

        assertTrue(updated);
        // Scheduled vehicle count should increase by 1
        assertEquals(3, serviceAdvisor.getScheduledVehicleCount());
        assertTrue(serviceAdvisor.getIsAvailable()); // Advisor should still be available

        // Verifying that userRepository.save was called with the updated advisor
        verify(userRepository, times(1)).save(serviceAdvisor);
    }
}