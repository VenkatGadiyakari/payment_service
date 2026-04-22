package com.ticketing.payment.controller;

import com.ticketing.payment.dto.CreateOrderRequest;
import com.ticketing.payment.dto.CreateOrderResponse;
import com.ticketing.payment.service.OrderService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/orders")
public class OrderController {

    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @PostMapping
    public ResponseEntity<CreateOrderResponse> createOrder(
            @Valid @RequestBody CreateOrderRequest request,
            @RequestHeader(value = "X-Buyer-Id", required = false) String buyerIdHeader) {

        UUID buyerId = buyerIdHeader != null ? UUID.fromString(buyerIdHeader) : UUID.randomUUID();

        CreateOrderResponse response = orderService.createOrder(request, buyerId);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
}
