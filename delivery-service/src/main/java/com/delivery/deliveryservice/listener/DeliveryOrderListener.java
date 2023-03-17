package com.delivery.deliveryservice.listener;


import com.delivery.deliveryservice.dto.event.DeliveryOrderEvent;
import com.delivery.deliveryservice.dto.event.OrderStatusChangeEvent;
import com.delivery.deliveryservice.service.DeliveryService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class DeliveryOrderListener {

    private final DeliveryService deliveryService;

    @RabbitListener(queues = "new_order_queue", messageConverter = "producerJackson2MessageConverter")
    public void receiveDeliveryOrderEvent(DeliveryOrderEvent event) {
        try {
            log.info("Received new delivery event from order-service: {}", event);
            deliveryService.processDeliveryOrderEvent(event);
        } catch (Exception e) {
            log.error("Error processing new delivery event from order-service: {}", e.getMessage());
        }
    }

    @RabbitListener(queues = "changed_order_queue", messageConverter = "producerJackson2MessageConverter")
    public void receiveOrderChangedEvent(OrderStatusChangeEvent event) {
        try {
            log.info("Received order changed event from order-service: {}", event);
            deliveryService.processOrderChangedEvent(event);
        } catch (Exception e) {
            log.error("Error processing order changed event from order-service: {}", e.getMessage());
        }
    }
}