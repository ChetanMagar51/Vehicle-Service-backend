package com.application.vehicleservicemanagement.service;

import com.application.vehicleservicemanagement.entity.User;
import com.application.vehicleservicemanagement.entity.Vehicle;

public interface AdvisorService {
    Boolean updateAdvisorStatusDuringScheduling(User user);

    Boolean updateAdvisorStatusAfterService(User user, Vehicle vehicle);
}
