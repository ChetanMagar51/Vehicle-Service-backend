package com.application.vehicleservicemanagement.scheduler;

import java.time.YearMonth;
import java.util.List;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.application.vehicleservicemanagement.entity.AdvisorAvailability;
import com.application.vehicleservicemanagement.entity.Role;
import com.application.vehicleservicemanagement.entity.User;
import com.application.vehicleservicemanagement.exception.ResourceNotFoundException;
import com.application.vehicleservicemanagement.repository.AdvisorAvailabilityRepository;
import com.application.vehicleservicemanagement.repository.UserRepository;
import com.application.vehicleservicemanagement.service.AdvisorScheduleService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Component
@RequiredArgsConstructor
@Slf4j
public class SlotGenerationScheduler {

    private final UserRepository userRepository;
    private final AdvisorAvailabilityRepository advisorAvailabilityRepository;
    private final AdvisorScheduleService advisorScheduleService;

    @Scheduled(cron = "0 0 0 1 * *")
    public void generateNextMonthSlots() {

        log.info("Generating next month slots...");

        YearMonth nextMonth =
                YearMonth.now().plusMonths(1);

        List<User> advisors =
                userRepository.findAllByRole(Role.SERVICE_ADVISOR);

        for (User advisor : advisors) {

            AdvisorAvailability availability =
                    advisorAvailabilityRepository
                            .findByServiceAdvisor(advisor)
                            .orElseThrow(() ->
                                    new ResourceNotFoundException(
                                            "AdvisorAvailability",
                                            "advisorId",
                                            advisor.getId().toString()));

            advisorScheduleService.generateMonthlySlots(
                    advisor,
                    availability,
                    nextMonth);
        }

        log.info(
                "Next month slot generation completed for {} advisors.",
                advisors.size());
    }
}