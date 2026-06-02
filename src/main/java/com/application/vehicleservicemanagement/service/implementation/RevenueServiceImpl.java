package com.application.vehicleservicemanagement.service.implementation;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import org.springframework.stereotype.Service;

import com.application.vehicleservicemanagement.dto.RevenueReportDto;
import com.application.vehicleservicemanagement.dto.RevenueSummaryDto;
import com.application.vehicleservicemanagement.entity.ServiceRecord;
import com.application.vehicleservicemanagement.repository.ServiceRecordRepository;
import com.application.vehicleservicemanagement.service.RevenueService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class RevenueServiceImpl implements RevenueService {

    private final ServiceRecordRepository serviceRecordRepository;

    @Override
    public RevenueSummaryDto getRevenueSummary() {

        List<ServiceRecord> paidRecords =
                serviceRecordRepository.findByIsPaymentCompleted(true);

        Double totalRevenue = paidRecords.stream()
                .mapToDouble(ServiceRecord::getAmount)
                .sum();

        LocalDate today = LocalDate.now();

        LocalDateTime startOfToday =
                today.atStartOfDay();

        LocalDateTime endOfToday =
                today.atTime(23, 59, 59);

        Double todayRevenue =
                serviceRecordRepository
                        .findByIsPaymentCompletedAndServiceDateBetween(
                                true,
                                startOfToday,
                                endOfToday)
                        .stream()
                        .mapToDouble(ServiceRecord::getAmount)
                        .sum();

        LocalDateTime monthStart =
                today.withDayOfMonth(1)
                        .atStartOfDay();

        LocalDateTime monthEnd =
                today.withDayOfMonth(today.lengthOfMonth())
                        .atTime(23, 59, 59);

        Double monthlyRevenue =
                serviceRecordRepository
                        .findByIsPaymentCompletedAndServiceDateBetween(
                                true,
                                monthStart,
                                monthEnd)
                        .stream()
                        .mapToDouble(ServiceRecord::getAmount)
                        .sum();

        Long totalServices =
                serviceRecordRepository.count();

        Long paidServices =
                (long) paidRecords.size();

        Long unpaidServices =
                serviceRecordRepository
                        .findByIsPaymentCompleted(false)
                        .size() * 1L;
        
        Double totalUnpaidAmount = serviceRecordRepository
                .findByIsPaymentCompleted(false)
                .stream()
                .mapToDouble(ServiceRecord::getAmount)
                .sum();

//        return RevenueSummaryDto.builder()
//                .totalRevenue(totalRevenue)
//                .todayRevenue(todayRevenue)
//                .monthlyRevenue(monthlyRevenue)
//                .totalServices(totalServices)
//                .totalPaidServices(paidServices)
//                .totalUnpaidServices(unpaidServices)
//                .build();
        
        return RevenueSummaryDto.builder()
                .totalRevenue(totalRevenue)
                .todayRevenue(todayRevenue)
                .monthlyRevenue(monthlyRevenue)
                .totalServices(totalServices)
                .totalPaidServices(paidServices)
                .totalUnpaidServices(unpaidServices)
                .totalUnpaidAmount(totalUnpaidAmount)
                .build();
    }
    
    @Override
    public RevenueReportDto getTodayRevenue() {

        LocalDate today = LocalDate.now();

        LocalDateTime startOfDay = today.atStartOfDay();
        LocalDateTime endOfDay = today.atTime(23, 59, 59);

        List<ServiceRecord> todayRecords =
                serviceRecordRepository.findByServiceDateBetween(startOfDay, endOfDay);

        long totalServices = todayRecords.size();

        List<ServiceRecord> paidRecords = todayRecords.stream()
                .filter(r -> Boolean.TRUE.equals(r.getIsPaymentCompleted()))
                .toList();

        List<ServiceRecord> unpaidRecords = todayRecords.stream()
                .filter(r -> !Boolean.TRUE.equals(r.getIsPaymentCompleted()))
                .toList();

        long paidServices = paidRecords.size();
        long unpaidServices = unpaidRecords.size();

        double revenue = paidRecords.stream()
                .mapToDouble(ServiceRecord::getAmount)
                .sum();

        double pendingAmount = unpaidRecords.stream()
                .mapToDouble(ServiceRecord::getAmount)
                .sum();

        double averageRevenuePerService = totalServices == 0
                ? 0.0
                : revenue / totalServices;

        return RevenueReportDto.builder()
                .revenue(revenue)
                .totalServices(totalServices)
                .paidServices(paidServices)
                .unpaidServices(unpaidServices)
                .pendingAmount(pendingAmount)
                .averageRevenuePerService(averageRevenuePerService)
                .build();
    }
    
    @Override
    public RevenueReportDto getYearlyRevenue() {

        LocalDate today = LocalDate.now();

        LocalDateTime startOfYear =
                today.withDayOfYear(1).atStartOfDay();

        LocalDateTime endOfYear =
                today.withMonth(12)
                        .withDayOfMonth(31)
                        .atTime(23, 59, 59);

        List<ServiceRecord> records =
                serviceRecordRepository.findByServiceDateBetween(
                        startOfYear,
                        endOfYear);

        return buildRevenueReport(records);
    }
    
    @Override
    public RevenueReportDto getMonthlyRevenue() {

        LocalDate today = LocalDate.now();

        LocalDateTime startOfMonth =
                today.withDayOfMonth(1).atStartOfDay();

        LocalDateTime endOfMonth =
                today.withDayOfMonth(today.lengthOfMonth())
                        .atTime(23, 59, 59);

        List<ServiceRecord> records =
                serviceRecordRepository.findByServiceDateBetween(
                        startOfMonth,
                        endOfMonth);

        return buildRevenueReport(records);
    }
    
    private RevenueReportDto buildRevenueReport(
            List<ServiceRecord> records) {

        long totalServices = records.size();

        long paidServices = records.stream()
                .filter(record ->
                        Boolean.TRUE.equals(
                                record.getIsPaymentCompleted()))
                .count();

        long unpaidServices = totalServices - paidServices;

        double revenue = records.stream()
                .filter(record ->
                        Boolean.TRUE.equals(
                                record.getIsPaymentCompleted()))
                .mapToDouble(ServiceRecord::getAmount)
                .sum();

        double pendingAmount = records.stream()
                .filter(record ->
                        Boolean.FALSE.equals(
                                record.getIsPaymentCompleted()))
                .mapToDouble(ServiceRecord::getAmount)
                .sum();

        double averageRevenuePerService = totalServices == 0
                ? 0.0
                : revenue / totalServices;

        return RevenueReportDto.builder()
                .revenue(revenue)
                .totalServices(totalServices)
                .paidServices(paidServices)
                .unpaidServices(unpaidServices)
                .averageRevenuePerService(averageRevenuePerService)
                .pendingAmount(pendingAmount)
                .build();
    }
}