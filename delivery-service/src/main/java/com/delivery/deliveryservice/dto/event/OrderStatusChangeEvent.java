package com.delivery.deliveryservice.dto.event;

import com.delivery.deliveryservice.model.OrderStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrderStatusChangeEvent implements Serializable {

    private static final long serialVersionUID = 1L;

    private String orderNumber;

    private OrderStatus orderStatus;

}
