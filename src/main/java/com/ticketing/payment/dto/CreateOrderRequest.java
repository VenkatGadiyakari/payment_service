package com.ticketing.payment.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

import java.util.UUID;

@Schema(description = "Internal request from order-service to create a Razorpay payment link for a single tier")
public class CreateOrderRequest {

    @Schema(description = "ID of the ticket tier", example = "d4e5f6a7-b8c9-0123-defa-234567890123")
    @NotNull(message = "Tier ID is required")
    private UUID tierId;

    @Schema(description = "Number of tickets", example = "2")
    @NotNull(message = "Quantity is required")
    @Min(value = 1, message = "Quantity must be at least 1")
    private Integer quantity;

    public CreateOrderRequest() {
    }

    public CreateOrderRequest(UUID tierId, Integer quantity) {
        this.tierId = tierId;
        this.quantity = quantity;
    }

    public UUID getTierId() {
        return tierId;
    }

    public void setTierId(UUID tierId) {
        this.tierId = tierId;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }
}
