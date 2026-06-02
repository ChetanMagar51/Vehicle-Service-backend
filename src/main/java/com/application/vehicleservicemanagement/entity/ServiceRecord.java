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
@Entity
@Table(name = "service_record")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ServiceRecord {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private LocalDateTime serviceDate;

    private Double amount;

    private Boolean isAdminApproved;

    private Boolean isPaymentCompleted;

    @ManyToOne
    @JoinColumn(name = "vehicle_id")
    private Vehicle vehicle;

    @ManyToOne
    @JoinColumn(name = "service_advisor_id")
    private User serviceAdvisor;

    @ElementCollection
    @CollectionTable(
        name = "service_record_items",
        joinColumns = @JoinColumn(name = "service_record_id")
    )
    @MapKeyJoinColumn(name = "item_id")
    @Column(name = "quantity")
    private Map<Item, Integer> itemQuantityMap;
}