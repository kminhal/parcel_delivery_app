package com.delivery.orderservice.service;


import com.delivery.orderservice.dto.*;
import com.delivery.orderservice.dto.event.DeliveryOrderEvent;
import com.delivery.orderservice.dto.event.OrderStatusChangeEvent;
import com.delivery.orderservice.model.Order;
import com.delivery.orderservice.model.OrderStatus;
import com.delivery.orderservice.repository.OrderRepository;
import com.delivery.orderservice.security.AuthUtil;
import com.delivery.orderservice.util.exception.OrderUpdateException;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class OrderService {
    private final OrderRepository orderRepository;
    private final ModelMapper mapper;
    private final RabbitTemplate template;

    @Value("${app.initial_coordinates}")
    private String init_coordinates;

    public List<OrderDto> getAllOrders() {
        List<Order> orders = AuthUtil.hasAdminRole() ? orderRepository.findAll() :
                orderRepository.findAllByCreatedBy(AuthUtil.getCurrentUserLogin());
        return orders.stream()
                .map(order -> mapper.map(order, OrderDto.class))
                .collect(Collectors.toList());
    }

    public OrderDto getOrder(Long id) {
        Optional<Order> order = AuthUtil.hasAdminRole() ? orderRepository.findById(id) :
                orderRepository.findByIdAndCreatedBy(id, AuthUtil.getCurrentUserLogin());
        return order.map(o -> mapper.map(o, OrderDto.class))
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
    }

    @Transactional
    public OrderDto createOrder(CreateOrderRequest req) {
        Order order = Order.builder()
                .orderNumber(UUID.randomUUID().toString())
                .product(req.getProduct())
                .destination(req.getDestination())
                .status(OrderStatus.CREATED)
                .createdBy(AuthUtil.getCurrentUserLogin())
                .build();
        order = orderRepository.save(order);
        return mapper.map(order, OrderDto.class);
    }

    @Transactional
    public OrderDto updateDestination(ChangeOrderDestinationRequest request) {
        Order order = orderRepository.findById(request.getOrderId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));

        if (OrderStatus.CREATED.equals(order.getStatus())) {
            order.setDestination(request.getNewDestination());
            order = orderRepository.save(order);
            return mapper.map(order, OrderDto.class);
        } else {
            throw new OrderUpdateException("You cannot update the direction in this order");
        }
    }

    @Transactional
    public OrderDto cancelOrder(Long orderId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        OrderStatus curStatus = order.getStatus();
        if (OrderStatus.CREATED.equals(curStatus) || OrderStatus.ASSIGNED.equals(curStatus)) {
            order.setStatus(OrderStatus.CANCELLED);
            order = orderRepository.save(order);
            if (OrderStatus.ASSIGNED.equals(curStatus)) {
                OrderStatusChangeEvent event = OrderStatusChangeEvent.builder()
                        .orderNumber(order.getOrderNumber())
                        .orderStatus(order.getStatus())
                        .build();
                template.convertAndSend("changed_order_queue", event);
            }
            return mapper.map(order, OrderDto.class);
        } else {
            throw new OrderUpdateException("You cannot cancel this order");
        }
    }

    @Transactional
    public OrderDto changeStatus(ChangeOrderStatusRequest request) {
        Order order = orderRepository.findById(request.getOrderId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        order.setStatus(request.getNewStatus());
        order = orderRepository.save(order);

        OrderStatusChangeEvent event = OrderStatusChangeEvent.builder()
                .orderNumber(order.getOrderNumber())
                .orderStatus(order.getStatus())
                .build();
        template.convertAndSend("changed_order_queue", event);

        return mapper.map(order, OrderDto.class);
    }

    @Transactional
    public void changeStatus(OrderStatusChangeEvent event) {
        Order order = orderRepository.findByOrderNumber(event.getOrderNumber())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        order.setStatus(event.getOrderStatus());
        orderRepository.save(order);
    }

    @Transactional
    public OrderDto assignCourier(AssignCourierRequest request) {
        Order order = orderRepository.findById(request.getOrderId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        if (OrderStatus.CREATED.equals(order.getStatus())) {
            order.setStatus(OrderStatus.ASSIGNED);
            order = orderRepository.save(order);

            DeliveryOrderEvent event = DeliveryOrderEvent.builder()
                    .orderNumber(order.getOrderNumber())
                    .destination(order.getDestination())
                    .status(order.getStatus())
                    .courier(request.getCourierUsername())
                    .coordination(init_coordinates)
                    .build();
            template.convertAndSend("new_order_queue", event);

            return mapper.map(order, OrderDto.class);
        } else {
            throw new OrderUpdateException("You cannot assign courier for this order");
        }
    }
}
