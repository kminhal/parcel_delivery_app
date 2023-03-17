package com.delivery.deliveryservice.dto;

import com.delivery.deliveryservice.model.OrderStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DeliveryDto {

    private Long id;

    private String orderNumber;

    private String destination;

    private String coordination;

    private OrderStatus status;

    private String courier;

}
