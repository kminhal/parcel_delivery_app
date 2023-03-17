package com.delivery.deliveryservice.dto;

import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class UpdateDeliveryCoordinatesRequest {

    @NotNull
    private Long deliveryId;

    @NotNull
    private String newCoordinates;
}
