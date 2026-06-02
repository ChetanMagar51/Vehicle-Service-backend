package com.application.vehicleservicemanagement.dto;

import lombok.Data;

@Data
public class StockRequestDto {
	private Long itemId;
    private Integer quantity;
    private String reason;

}
