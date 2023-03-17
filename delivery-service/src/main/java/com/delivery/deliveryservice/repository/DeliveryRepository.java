package com.delivery.deliveryservice.repository;

import com.delivery.deliveryservice.model.Delivery;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface DeliveryRepository extends JpaRepository<Delivery, Long> {

    List<Delivery> findAllByCourier(String courier);

    Optional<Delivery> findByIdAndCourier(Long id, String courier);

    Optional<Delivery> findByOrderNumber(String orderNumber);

}