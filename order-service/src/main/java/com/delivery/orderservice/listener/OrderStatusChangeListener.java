package com.delivery.orderservice.listener;

import com.delivery.orderservice.dto.event.OrderStatusChangeEvent;
import com.delivery.orderservice.service.OrderService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class OrderStatusChangeListener {

    private final OrderService orderService;

    @RabbitListener(queues = "delivery_queue", messageConverter = "producerJackson2MessageConverter")
    public void receiveOrderStatusChangeEvent(OrderStatusChangeEvent event) {
        try {
            log.info("Receive new order status from delivery-service: {}", event);
            orderService.changeStatus(event);
        }catch (Exception e){
            log.error("Error processing new order status from delivery-service: {}", e.getMessage());
        }
    }
}