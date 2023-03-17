package com.delivery.orderservice.dto;

import com.delivery.orderservice.model.OrderStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrderDto {

    private Long id;

    @NotBlank
    private String orderNumber;

    @NotBlank
    private String product;

    @NotBlank
    private String destination;

    @NotBlank
    private OrderStatus status;

    private String createdBy;
}
