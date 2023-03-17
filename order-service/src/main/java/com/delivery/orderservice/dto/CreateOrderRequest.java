package com.delivery.orderservice.dto;

import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class CreateOrderRequest {

    @NotBlank
    private String product;

    @NotBlank
    private String destination;
}
