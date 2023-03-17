package com.delivery.orderservice.dto;

import com.delivery.orderservice.model.OrderStatus;
import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class ChangeOrderStatusRequest {

    @NotNull
    private Long orderId;

    @NotNull
    private OrderStatus newStatus;
}
