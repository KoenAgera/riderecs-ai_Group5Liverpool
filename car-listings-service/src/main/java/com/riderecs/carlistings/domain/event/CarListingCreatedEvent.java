package com.riderecs.carlistings.domain.event;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CarListingCreatedEvent {
    private Long carId;
    private String make;
    private String model;
    private Integer year;
    private Double price;
    private Integer mileage;
    private String condition;
    private Long sellerId;
    private LocalDateTime createdAt;
}
