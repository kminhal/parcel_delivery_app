package com.delivery.orderservice.repository;

import com.delivery.orderservice.model.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {

    List<Order> findAllByCreatedBy(String createdBy);

    Optional<Order> findByIdAndCreatedBy(Long id, String createdBy);

    Optional<Order> findByOrderNumber(String orderNumber);

}