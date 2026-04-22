package com.ticketing.payment.entity;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class OrderTest {

    @Test
    void testNoArgsConstructor() {
        Order order = new Order();
        assertNotNull(order);
        assertEquals(OrderStatus.PENDING, order.getStatus());
    }

    @Test
    void testAllArgsConstructor() {
        UUID buyerId = UUID.randomUUID();
        BigDecimal totalAmount = BigDecimal.valueOf(500.00);
        String stripeSessionId = "cs_test_123456";

        Order order = new Order(buyerId, OrderStatus.CONFIRMED, totalAmount, stripeSessionId);

        assertEquals(buyerId, order.getBuyerId());
        assertEquals(OrderStatus.CONFIRMED, order.getStatus());
        assertEquals(totalAmount, order.getTotalAmount());
        assertEquals(stripeSessionId, order.getStripeSessionId());
    }

    @Test
    void testSettersAndGetters() {
        Order order = new Order();
        UUID id = UUID.randomUUID();
        UUID buyerId = UUID.randomUUID();
        BigDecimal totalAmount = BigDecimal.valueOf(750.50);
        String stripeSessionId = "cs_test_789012";

        order.setId(id);
        order.setBuyerId(buyerId);
        order.setStatus(OrderStatus.FAILED);
        order.setTotalAmount(totalAmount);
        order.setStripeSessionId(stripeSessionId);

        assertEquals(id, order.getId());
        assertEquals(buyerId, order.getBuyerId());
        assertEquals(OrderStatus.FAILED, order.getStatus());
        assertEquals(totalAmount, order.getTotalAmount());
        assertEquals(stripeSessionId, order.getStripeSessionId());
    }

    @Test
    void testDefaultStatus() {
        Order order = new Order();
        assertEquals(OrderStatus.PENDING, order.getStatus());
    }

    @Test
    void testTimestampFields() {
        Order order = new Order();
        Instant createdAt = Instant.now();
        Instant updatedAt = Instant.now().plusSeconds(10);

        order.setCreatedAt(createdAt);
        order.setUpdatedAt(updatedAt);

        assertEquals(createdAt, order.getCreatedAt());
        assertEquals(updatedAt, order.getUpdatedAt());
    }

    @Test
    void testStatusTransitions() {
        Order order = new Order();

        assertEquals(OrderStatus.PENDING, order.getStatus());

        order.setStatus(OrderStatus.CONFIRMED);
        assertEquals(OrderStatus.CONFIRMED, order.getStatus());

        order.setStatus(OrderStatus.FAILED);
        assertEquals(OrderStatus.FAILED, order.getStatus());
    }
}
