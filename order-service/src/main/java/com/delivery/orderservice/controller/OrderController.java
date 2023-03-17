package com.delivery.orderservice.controller;

import com.delivery.orderservice.dto.*;
import com.delivery.orderservice.service.OrderService;
import com.delivery.orderservice.util.exception.OrderUpdateException;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.List;

@RestController
@RequestMapping("/order")
@RequiredArgsConstructor
@Tag(
        name = "Order",
        description = "Order service"
)
public class OrderController {

    private final OrderService orderService;

    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_USER')")
    @Operation(summary = "User can see all parcel delivery orders that he/she created. Admin can view all parcel delivery orders")
    @GetMapping()
    public List<OrderDto> getAllOrders() {
        return orderService.getAllOrders();
    }

    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_USER')")
    @Operation(summary = "User can see the details of a delivery")
    @GetMapping("/{orderId}")
    public ResponseEntity<OrderDto> getOrder(@NotNull @PathVariable Long orderId) {
        return ResponseEntity.ok(orderService.getOrder(orderId));
    }

    @PreAuthorize("hasRole('ROLE_USER')")
    @Operation(summary = "User can create a parcel delivery order")
    @PostMapping
    public ResponseEntity<OrderDto> createOrder(@Valid @RequestBody CreateOrderRequest order) {
        return ResponseEntity.status(HttpStatus.CREATED).body(orderService.createOrder(order));
    }

    @PreAuthorize("hasRole('ROLE_USER')")
    @Operation(summary = "User can change the destination of a parcel delivery order (only if the status is CREATED)")
    @PutMapping("/change-destination")
    public ResponseEntity<OrderDto> updateDestination(@Valid @RequestBody ChangeOrderDestinationRequest request) {
        return ResponseEntity.ok(orderService.updateDestination(request));
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @Operation(summary = "Admin can change the status of a parcel delivery order")
    @PutMapping("/change-status")
    public ResponseEntity<OrderDto> changeStatus(@Valid @RequestBody ChangeOrderStatusRequest request) {
        return ResponseEntity.ok(orderService.changeStatus(request));
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @Operation(summary = "Admin can assign parcel delivery order to courier (only if the status is CREATED)")
    @PutMapping("/assign-courier")
    public ResponseEntity<OrderDto> assignCourier(@Valid @RequestBody AssignCourierRequest request) {
        return ResponseEntity.ok(orderService.assignCourier(request));
    }

    @PreAuthorize("hasRole('ROLE_USER')")
    @Operation(summary = "User can cancel a parcel delivery order (only if the status is CREATED or ASSIGNED)")
    @PutMapping("/cancel/{orderId}")
    public ResponseEntity<OrderDto> cancelOrder(@NotNull @PathVariable Long orderId) {
        return ResponseEntity.ok(orderService.cancelOrder(orderId));
    }

    @ExceptionHandler
    private ResponseEntity<MessageResponse> catchResponseStatusException(ResponseStatusException e) {
        return new ResponseEntity<>(new MessageResponse(e.getMessage()), e.getStatus());
    }

    @ExceptionHandler
    private ResponseEntity<MessageResponse> catchOrderUpdateException(OrderUpdateException e) {
        return new ResponseEntity<>(new MessageResponse(e.getMessage()), HttpStatus.BAD_REQUEST);
    }
}