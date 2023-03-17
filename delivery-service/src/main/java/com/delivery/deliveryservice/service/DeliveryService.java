package com.delivery.deliveryservice.service;


import com.delivery.deliveryservice.dto.ChangeDeliveryStatusRequest;
import com.delivery.deliveryservice.dto.DeliveryDto;
import com.delivery.deliveryservice.dto.UpdateDeliveryCoordinatesRequest;
import com.delivery.deliveryservice.dto.event.DeliveryOrderEvent;
import com.delivery.deliveryservice.dto.event.OrderStatusChangeEvent;
import com.delivery.deliveryservice.model.Delivery;
import com.delivery.deliveryservice.repository.DeliveryRepository;
import com.delivery.deliveryservice.security.AuthUtil;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;


@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class DeliveryService {

    private final DeliveryRepository deliveryRepository;
    private final ModelMapper mapper;

    private final RabbitTemplate template;

    public List<DeliveryDto> getAllDeliveries() {
        List<Delivery> deliveries = AuthUtil.hasAdminRole() ? deliveryRepository.findAll() :
                deliveryRepository.findAllByCourier(AuthUtil.getCurrentUserLogin());
        return deliveries.stream()
                .map(delivery -> mapper.map(delivery, DeliveryDto.class))
                .collect(Collectors.toList());
    }

    public DeliveryDto getDelivery(Long id) {
        Optional<Delivery> delivery = AuthUtil.hasAdminRole() ? deliveryRepository.findById(id) :
                deliveryRepository.findByIdAndCourier(id, AuthUtil.getCurrentUserLogin());
        return delivery.map(d -> mapper.map(d, DeliveryDto.class))
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
    }

    @Transactional
    public DeliveryDto changeStatus(ChangeDeliveryStatusRequest request) {
        Delivery delivery = deliveryRepository.findById(request.getDeliveryId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        delivery.setStatus(request.getNewStatus());
        delivery = deliveryRepository.save(delivery);

        OrderStatusChangeEvent event = OrderStatusChangeEvent.builder()
                .orderNumber(delivery.getOrderNumber())
                .orderStatus(delivery.getStatus())
                .build();
        template.convertAndSend("delivery_queue", event);

        return mapper.map(delivery, DeliveryDto.class);
    }

    @Transactional
    public DeliveryDto updateCoordinates(UpdateDeliveryCoordinatesRequest request) {
        Delivery delivery = deliveryRepository.findById(request.getDeliveryId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        delivery.setCoordination(request.getNewCoordinates());
        delivery = deliveryRepository.save(delivery);
        return mapper.map(delivery, DeliveryDto.class);
    }

    public String trackDelivery(Long id) {
        Delivery delivery = deliveryRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        return delivery.getCoordination();
    }

    @Transactional
    public void processDeliveryOrderEvent(DeliveryOrderEvent event) {
        Optional<Delivery> maybeDelivery = deliveryRepository.findByOrderNumber(event.getOrderNumber());
        Delivery delivery = Delivery.builder()
                .orderNumber(event.getOrderNumber())
                .destination(event.getDestination())
                .coordination(event.getCoordination())
                .status(event.getStatus())
                .courier(event.getCourier())
                .build();
        maybeDelivery.ifPresent(value -> delivery.setId(value.getId()));

        deliveryRepository.save(delivery);
    }

    @Transactional
    public void processOrderChangedEvent(OrderStatusChangeEvent event) {
        Delivery delivery = deliveryRepository.findByOrderNumber(event.getOrderNumber())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        delivery.setStatus(event.getOrderStatus());
        deliveryRepository.save(delivery);
    }
}
