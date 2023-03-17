package com.delivery.gateway.config;

import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class GatewayConfig {

    @Bean
    public RouteLocator gatewayRoutes(RouteLocatorBuilder builder) {
        return builder.routes()
                .route("auth", r -> r.path("/auth/**").uri("lb://auth-service"))
                .route("user", r -> r.path("/user/**").uri("lb://auth-service"))
                .route("order", r -> r.path("/order/**").uri("lb://order-service"))
                .route("delivery", r -> r.path("/delivery/**").uri("lb://delivery-service"))
                .build();
    }

}
