package com.application.vehicleservicemanagement.scheduler;

import java.time.YearMonth;
import java.util.List;

import org.springframework.stereotype.Component;

import com.application.vehicleservicemanagement.entity.Role;
import com.application.vehicleservicemanagement.entity.User;
import com.application.vehicleservicemanagement.repository.AdvisorAvailabilityRepository;
import com.application.vehicleservicemanagement.repository.UserRepository;
import com.application.vehicleservicemanagement.service.AdvisorScheduleService;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Component
@RequiredArgsConstructor
@Slf4j
public class SlotInitializationRunner {

    private final UserRepository userRepository;
    private final AdvisorAvailabilityRepository advisorAvailabilityRepository;
    private final AdvisorScheduleService advisorScheduleService;

    @PostConstruct
    public void initializeSlots() {

        log.info("Starting advisor slot initialization...");

        YearMonth currentMonth = YearMonth.now();
        YearMonth nextMonth = currentMonth.plusMonths(1);

        List<User> advisors =
                userRepository.findAllByRole(Role.SERVICE_ADVISOR);

        for (User advisor : advisors) {

            advisorAvailabilityRepository
                    .findByServiceAdvisor(advisor)
                    .ifPresent(availability -> {

                        advisorScheduleService.generateMonthlySlots(
                                advisor,
                                availability,
                                currentMonth);

                        advisorScheduleService.generateMonthlySlots(
                                advisor,
                                availability,
                                nextMonth);
                    });
        }

        log.info(
                "Advisor slot initialization completed for {} advisors",
                advisors.size());
    }
}