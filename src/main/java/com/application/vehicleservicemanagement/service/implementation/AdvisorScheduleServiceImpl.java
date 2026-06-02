package com.application.vehicleservicemanagement.service.implementation;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.YearMonth;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.application.vehicleservicemanagement.entity.AdvisorAvailability;
import com.application.vehicleservicemanagement.entity.AdvisorSchedule;
import com.application.vehicleservicemanagement.entity.ScheduleStatus;
import com.application.vehicleservicemanagement.entity.User;
import com.application.vehicleservicemanagement.repository.AdvisorScheduleRepository;
import com.application.vehicleservicemanagement.service.AdvisorScheduleService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AdvisorScheduleServiceImpl implements AdvisorScheduleService {

    private final AdvisorScheduleRepository advisorScheduleRepository;

    @Override
    @Transactional
    public void generateMonthlySlots(
            User advisor,
            AdvisorAvailability availability,
            YearMonth yearMonth) {

        LocalDate currentDate = yearMonth.atDay(1);
        LocalDate endDate = yearMonth.atEndOfMonth();

        while (!currentDate.isAfter(endDate)) {

            generateDailySlots(
                    advisor,
                    availability,
                    currentDate);

            currentDate = currentDate.plusDays(1);
        }
    }

    private void generateDailySlots(
            User advisor,
            AdvisorAvailability availability,
            LocalDate date) {

        LocalTime currentTime =
                availability.getWorkStartTime();

        while (currentTime.plusMinutes(
                availability.getSlotDuration())
                .compareTo(
                        availability.getWorkEndTime()) <= 0) {

            boolean exists =
                    advisorScheduleRepository
                            .existsByServiceAdvisorAndScheduleDateAndStartTime(
                                    advisor,
                                    date,
                                    currentTime);

            if (!exists) {

                LocalTime endTime =
                        currentTime.plusMinutes(
                                availability.getSlotDuration());

                AdvisorSchedule schedule =
                        AdvisorSchedule.builder()
                                .serviceAdvisor(advisor)
                                .scheduleDate(date)
                                .startTime(currentTime)
                                .endTime(endTime)
                                .status(
                                        ScheduleStatus.AVAILABLE)
                                .build();

                advisorScheduleRepository.save(
                        schedule);
            }

            currentTime =
                    currentTime.plusMinutes(
                            availability.getSlotDuration());
        }
    }
}