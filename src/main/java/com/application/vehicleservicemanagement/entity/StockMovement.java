package com.application.vehicleservicemanagement.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "stock_movement")
public class StockMovement {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // 🔥 Item reference
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "item_id", nullable = false)
    private Item item;

    // 🔥 IN / OUT type
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private MovementType type;

    // 🔥 quantity moved
    @Column(nullable = false)
    private Integer quantity;

    // 🔥 reason (service, purchase, return etc.)
    private String reason;

    // 🔥 date of movement
    private LocalDate date;

    // ⭐ IMPORTANT: audit field (who/when created)
    private LocalDateTime createdAt;

    // ⭐ IMPORTANT: snapshot price (VERY useful for invoices)
    private Double unitPriceAtThatTime;
}