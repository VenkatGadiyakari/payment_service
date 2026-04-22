package com.ticketing.payment.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public class CreateOrderRequest {

    @NotNull(message = "Tier ID is required")
    private UUID tierId;

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
