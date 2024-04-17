package com.application.vehicleservicemanagement.entity;

import jakarta.persistence.*;
import jdk.jfr.BooleanFlag;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "service_record")
public class ServiceRecord {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @DateTimeFormat
    private LocalDateTime date;

    @ElementCollection
    private Map<Item, Integer> itemQuantityMap;

    private Double amount;

    @BooleanFlag
    private Boolean isPaymentCompleted;

    @BooleanFlag
    private Boolean isAdminApproved;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User serviceAdvisor;

    @OneToOne(fetch = FetchType.LAZY)
    private Vehicle vehicle;
}
