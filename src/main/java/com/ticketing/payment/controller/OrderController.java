package com.ticketing.payment.controller;

import com.ticketing.payment.dto.CreateOrderRequest;
import com.ticketing.payment.dto.CreateOrderResponse;
import com.ticketing.payment.service.OrderService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@Tag(name = "Payment Orders", description = "Internal endpoint called by order-service to create a Razorpay payment link")
@RestController
@RequestMapping("/api/orders")
public class OrderController {

    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @Operation(summary = "Create Razorpay payment link",
            description = "Called internally by the order-service. Validates tier inventory, creates a Razorpay payment link, and reserves inventory. X-Buyer-Id header is passed by the order-service.")
    @ApiResponses({
        @ApiResponse(responseCode = "201", description = "Payment link created — contains Razorpay URL"),
        @ApiResponse(responseCode = "400", description = "Validation error or insufficient inventory", content = @Content),
        @ApiResponse(responseCode = "404", description = "Tier not found", content = @Content)
    })
    @PostMapping
    public ResponseEntity<CreateOrderResponse> createOrder(
            @Valid @RequestBody CreateOrderRequest request,
            @Parameter(description = "Buyer UUID forwarded from order-service") @RequestHeader(value = "X-Buyer-Id", required = false) String buyerIdHeader) {

        UUID buyerId = buyerIdHeader != null ? UUID.fromString(buyerIdHeader) : UUID.randomUUID();

        CreateOrderResponse response = orderService.createOrder(request, buyerId);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
}
