package com.application.vehicleservicemanagement;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

import io.swagger.v3.oas.models.annotations.OpenAPI30;
@OpenAPI30
@SpringBootApplication
@EnableScheduling
public class VehicleServiceManagementApplication {

	public static void main(String[] args) {
		SpringApplication.run(VehicleServiceManagementApplication.class, args);
	}

}
