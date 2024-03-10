package com.application.vehicleservicemanagement.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "service_advisor")
public class ServiceAdvisor extends User {
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "serviceAdvisor")
    private List<Vehicle> vehicleList;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "serviceAdvisor")
    private List<ServiceRecord> serviceRecordList;
}
