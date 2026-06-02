package com.application.vehicleservicemanagement.dto;


import java.time.LocalDate;
import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class StockMovementDto {

    private Long id;

    // Item info (avoid exposing full entity)
    private Long itemId;
    private String itemName;

    // IN / OUT
    private String type;

    private Integer quantity;

    private String reason;

    private LocalDate date;

    // audit fields
    private LocalDateTime createdAt;

    // important for invoice/history
    private Double unitPriceAtThatTime;
}