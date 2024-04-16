package com.application.vehicleservicemanagement.service.implementation;

import com.application.vehicleservicemanagement.configuration.ApplicationConfiguration;
import com.application.vehicleservicemanagement.entity.User;
import com.application.vehicleservicemanagement.entity.Vehicle;
import com.application.vehicleservicemanagement.repository.UserRepository;
import com.application.vehicleservicemanagement.service.AdvisorService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
@RequiredArgsConstructor
public class AdvisorServiceImplementation implements AdvisorService {
    private final UserRepository userRepository;

    @Override
    public Boolean updateAdvisorStatusDuringScheduling(User serviceAdvisor) {
        if (Objects.equals(serviceAdvisor.getScheduledVehicleCount(), ApplicationConfiguration.MAX_SCHEDULED_VEHICLE_COUNT) || !serviceAdvisor.getIsAvailable()) {
            return Boolean.FALSE;
        }
        serviceAdvisor.setScheduledVehicleCount(serviceAdvisor.getScheduledVehicleCount() + 1);
        if (Objects.equals(serviceAdvisor.getScheduledVehicleCount(), ApplicationConfiguration.MAX_SCHEDULED_VEHICLE_COUNT)) {
            serviceAdvisor.setIsAvailable(Boolean.FALSE);
        }
        userRepository.save(serviceAdvisor);
        return Boolean.TRUE;
    }

    @Override
    public Boolean updateAdvisorStatusAfterService(User serviceAdvisor, Vehicle vehicle) {
        serviceAdvisor.getVehicleList().remove(vehicle);
        serviceAdvisor.setVehicleList(serviceAdvisor.getVehicleList());
        serviceAdvisor.setScheduledVehicleCount(serviceAdvisor.getScheduledVehicleCount() - 1);
        serviceAdvisor.setIsAvailable(Boolean.TRUE);
        userRepository.save(serviceAdvisor);
        return Boolean.TRUE;
    }
}
