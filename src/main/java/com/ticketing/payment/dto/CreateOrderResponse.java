package com.ticketing.payment.dto;

import com.ticketing.payment.entity.OrderStatus;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

public class CreateOrderResponse {

    private UUID orderId;
    private UUID buyerId;
    private OrderStatus status;
    private BigDecimal totalAmount;
    private Instant createdAt;

    public CreateOrderResponse() {
    }

    public CreateOrderResponse(UUID orderId, UUID buyerId, OrderStatus status, BigDecimal totalAmount, Instant createdAt) {
        this.orderId = orderId;
        this.buyerId = buyerId;
        this.status = status;
        this.totalAmount = totalAmount;
        this.createdAt = createdAt;
    }

    public UUID getOrderId() {
        return orderId;
    }

    public void setOrderId(UUID orderId) {
        this.orderId = orderId;
    }

    public UUID getBuyerId() {
        return buyerId;
    }

    public void setBuyerId(UUID buyerId) {
        this.buyerId = buyerId;
    }

    public OrderStatus getStatus() {
        return status;
    }

    public void setStatus(OrderStatus status) {
        this.status = status;
    }

    public BigDecimal getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(BigDecimal totalAmount) {
        this.totalAmount = totalAmount;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
    }
}
