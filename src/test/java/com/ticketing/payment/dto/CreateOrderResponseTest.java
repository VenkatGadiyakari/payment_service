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
        String url = "https://rzp.io/l/test123";

        CreateOrderResponse response = new CreateOrderResponse(orderId, url);

        assertEquals(orderId, response.getOrderId());
        assertEquals(url, response.getCheckoutUrl());
    }

    @Test
    void testSettersAndGetters() {
        CreateOrderResponse response = new CreateOrderResponse();
        UUID orderId = UUID.randomUUID();
        String url = "https://rzp.io/l/test456";

        response.setOrderId(orderId);
        response.setCheckoutUrl(url);

        assertEquals(orderId, response.getOrderId());
        assertEquals(url, response.getCheckoutUrl());
    }

    @Test
    void testNullValues() {
        CreateOrderResponse response = new CreateOrderResponse(null, null);

        assertNull(response.getOrderId());
        assertNull(response.getCheckoutUrl());
    }
}
