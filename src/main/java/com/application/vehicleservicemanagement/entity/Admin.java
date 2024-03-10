package com.application.vehicleservicemanagement.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@Entity
@Table(name = "admin")
public class Admin extends User {
}
