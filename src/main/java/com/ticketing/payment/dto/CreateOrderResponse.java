package com.ticketing.payment.dto;

import java.util.UUID;

public class CreateOrderResponse {

    private UUID orderId;
    private String stripeCheckoutUrl;

    public CreateOrderResponse() {
    }

    public CreateOrderResponse(UUID orderId, String stripeCheckoutUrl) {
        this.orderId = orderId;
        this.stripeCheckoutUrl = stripeCheckoutUrl;
    }

    public UUID getOrderId() {
        return orderId;
    }

    public void setOrderId(UUID orderId) {
        this.orderId = orderId;
    }

    public String getStripeCheckoutUrl() {
        return stripeCheckoutUrl;
    }

    public void setStripeCheckoutUrl(String stripeCheckoutUrl) {
        this.stripeCheckoutUrl = stripeCheckoutUrl;
    }
}
