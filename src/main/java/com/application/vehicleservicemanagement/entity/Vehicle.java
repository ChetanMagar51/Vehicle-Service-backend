package com.application.vehicleservicemanagement.entity;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.springframework.format.annotation.DateTimeFormat;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "vehicle")
public class Vehicle {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String vehicleNumber;

    private String vehicleModel;

    private String vehicleDescription;
 
    private Integer queueNumber;

    @DateTimeFormat
    private LocalDateTime deliveryTime;

    @ManyToOne
    @JoinColumn(name = "owner_id")
    private Owner owner;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private ServiceStatus serviceStatus;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User serviceAdvisor;

    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private ServiceRecord serviceRecord;
    
    @OneToMany(mappedBy = "vehicle", cascade = CascadeType.ALL)
    private List<ServiceRecord> serviceRecords = new ArrayList<>();
    
    
    @ManyToOne
    @JoinColumn(name = "advisor_schedule_id")
    private AdvisorSchedule schedule;
    
    
}
