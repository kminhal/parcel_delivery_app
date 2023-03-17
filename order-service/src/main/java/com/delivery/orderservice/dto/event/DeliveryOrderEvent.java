package com.delivery.orderservice.dto.event;


import com.delivery.orderservice.model.OrderStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DeliveryOrderEvent implements Serializable {

    private static final long serialVersionUID = 1L;

    private String orderNumber;

    private String destination;

    private OrderStatus status;

    private String courier;

    private String coordination;
}