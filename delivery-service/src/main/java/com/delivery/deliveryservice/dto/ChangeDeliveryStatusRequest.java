package com.delivery.deliveryservice.dto;

import com.delivery.deliveryservice.model.OrderStatus;
import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class ChangeDeliveryStatusRequest {

    @NotNull
    private Long deliveryId;

    @NotNull
    private OrderStatus newStatus;

}
