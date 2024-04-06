package com.application.vehicleservicemanagement.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;

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
}
