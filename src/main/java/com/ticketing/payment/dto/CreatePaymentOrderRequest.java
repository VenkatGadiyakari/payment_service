package com.ticketing.payment.dto;

import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public class CreatePaymentOrderRequest {

    @NotNull(message = "Order ID is required")
    private UUID orderId;

    public CreatePaymentOrderRequest() {
    }

    public CreatePaymentOrderRequest(UUID orderId) {
        this.orderId = orderId;
    }

    public UUID getOrderId() {
        return orderId;
    }

    public void setOrderId(UUID orderId) {
        this.orderId = orderId;
    }
}
