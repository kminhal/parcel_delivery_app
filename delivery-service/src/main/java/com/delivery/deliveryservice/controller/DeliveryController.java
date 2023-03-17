package com.delivery.deliveryservice.controller;


import com.delivery.deliveryservice.dto.MessageResponse;
import com.delivery.deliveryservice.dto.UpdateDeliveryCoordinatesRequest;
import com.delivery.deliveryservice.service.DeliveryService;
import com.delivery.deliveryservice.dto.ChangeDeliveryStatusRequest;
import com.delivery.deliveryservice.dto.DeliveryDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.List;

@RestController
@RequestMapping("/delivery")
@RequiredArgsConstructor
@Tag(
        name = "Delivery",
        description = "Delivery service"
)
public class DeliveryController {

    private final DeliveryService deliveryService;

    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_COURIER')")
    @Operation(summary = "Courier can view all parcel delivery orders that assigned to him. Admin can view all parcel delivery orders")
    @GetMapping
    public List<DeliveryDto> getAllDeliveries() {
        return deliveryService.getAllDeliveries();
    }

    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_COURIER')")
    @Operation(summary = "Courier and Admin can see the details of a delivery order")
    @GetMapping("/{id}")
    public ResponseEntity<DeliveryDto> getDelivery(@NotNull @PathVariable Long id) {
        DeliveryDto deliveryDto = deliveryService.getDelivery(id);
        return ResponseEntity.ok(deliveryDto);
    }

    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_COURIER')")
    @Operation(summary = "Courier and Admin can change the status of a parcel delivery order")
    @PutMapping("/change-status")
    public ResponseEntity<DeliveryDto> changeStatus(@Valid @RequestBody ChangeDeliveryStatusRequest request) {
        return ResponseEntity.ok(deliveryService.changeStatus(request));
    }

    @PreAuthorize("hasRole('ROLE_COURIER')")
    @Operation(summary = "Courier can update the coordinates of a parcel delivery order")
    @PutMapping("/update-coordinates")
    public ResponseEntity<DeliveryDto> updateCoordinates(@Valid @RequestBody UpdateDeliveryCoordinatesRequest request) {
        return ResponseEntity.ok(deliveryService.updateCoordinates(request));
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @Operation(summary = "Admin can track the delivery order by coordinates")
    @GetMapping("/track/{id}")
    public ResponseEntity<String> trackDelivery(@NotNull @PathVariable Long id) {
        return ResponseEntity.ok(deliveryService.trackDelivery(id));
    }

    @ExceptionHandler
    private ResponseEntity<MessageResponse> catchResponseStatusException(ResponseStatusException e) {
        return new ResponseEntity<>(new MessageResponse(e.getMessage()), e.getStatus());
    }
}
