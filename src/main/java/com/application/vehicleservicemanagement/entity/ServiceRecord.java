package com.application.vehicleservicemanagement.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;

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

    private Date date;

    private Integer quantity;

    @OneToMany
    private List<Item> itemList;

    private Double amount;

    @ManyToOne
    @JoinColumn(name = "service_advisor_id")
    private User serviceAdvisor;

    @OneToOne(fetch = FetchType.LAZY)
    private Vehicle vehicle;
}
