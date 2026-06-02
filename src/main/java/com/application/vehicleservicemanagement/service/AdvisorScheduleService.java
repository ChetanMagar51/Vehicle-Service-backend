package com.application.vehicleservicemanagement.service;

import java.time.YearMonth;

import com.application.vehicleservicemanagement.entity.AdvisorAvailability;
import com.application.vehicleservicemanagement.entity.User;

public interface AdvisorScheduleService {
	void generateMonthlySlots(
            User advisor,
            AdvisorAvailability availability,
            YearMonth yearMonth);
}
