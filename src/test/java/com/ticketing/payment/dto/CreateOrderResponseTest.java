package com.ticketing.payment.dto;

import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class CreateOrderResponseTest {

    @Test
    void testNoArgsConstructor() {
        CreateOrderResponse response = new CreateOrderResponse();
        assertNotNull(response);
    }

    @Test
    void testAllArgsConstructor() {
        UUID orderId = UUID.randomUUID();
        String url = "https://checkout.stripe.com/pay/cs_test_123";

        CreateOrderResponse response = new CreateOrderResponse(orderId, url);

        assertEquals(orderId, response.getOrderId());
        assertEquals(url, response.getStripeCheckoutUrl());
    }

    @Test
    void testSettersAndGetters() {
        CreateOrderResponse response = new CreateOrderResponse();
        UUID orderId = UUID.randomUUID();
        String url = "https://checkout.stripe.com/pay/cs_test_456";

        response.setOrderId(orderId);
        response.setStripeCheckoutUrl(url);

        assertEquals(orderId, response.getOrderId());
        assertEquals(url, response.getStripeCheckoutUrl());
    }

    @Test
    void testNullValues() {
        CreateOrderResponse response = new CreateOrderResponse(null, null);

        assertNull(response.getOrderId());
        assertNull(response.getStripeCheckoutUrl());
    }
}
