package com.application.vehicleservicemanagement.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ItemDetailDto {

    private Integer quantity;

    private Double price;

    private Double totalPrice;
}