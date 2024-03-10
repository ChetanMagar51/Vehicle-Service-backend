package com.application.vehicleservicemanagement.entity;

import jakarta.persistence.*;
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

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private ServiceStatus serviceStatus;

    @ManyToOne
    @JoinColumn(name = "service_advisor_id")
    private ServiceAdvisor serviceAdvisor;

    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private ServiceRecord serviceRecord;
}
