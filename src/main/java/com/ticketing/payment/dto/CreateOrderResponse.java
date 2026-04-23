package com.ticketing.payment.dto;

import java.util.UUID;

public class CreateOrderResponse {

    private UUID orderId;
    private String checkoutUrl;

    public CreateOrderResponse() {
    }

    public CreateOrderResponse(UUID orderId, String checkoutUrl) {
        this.orderId = orderId;
        this.checkoutUrl = checkoutUrl;
    }

    public UUID getOrderId() {
        return orderId;
    }

    public void setOrderId(UUID orderId) {
        this.orderId = orderId;
    }

    public String getCheckoutUrl() {
        return checkoutUrl;
    }

    public void setCheckoutUrl(String checkoutUrl) {
        this.checkoutUrl = checkoutUrl;
    }
}
