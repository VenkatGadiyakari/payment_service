package com.ticketing.payment.entity;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class EnumTest {

    @Test
    void testOrderStatusValues() {
        assertEquals(3, OrderStatus.values().length);
        assertEquals(OrderStatus.PENDING, OrderStatus.valueOf("PENDING"));
        assertEquals(OrderStatus.CONFIRMED, OrderStatus.valueOf("CONFIRMED"));
        assertEquals(OrderStatus.FAILED, OrderStatus.valueOf("FAILED"));
    }

    @Test
    void testPaymentStatusValues() {
        assertEquals(3, PaymentStatus.values().length);
        assertEquals(PaymentStatus.PENDING, PaymentStatus.valueOf("PENDING"));
        assertEquals(PaymentStatus.SUCCEEDED, PaymentStatus.valueOf("SUCCEEDED"));
        assertEquals(PaymentStatus.FAILED, PaymentStatus.valueOf("FAILED"));
    }

    @Test
    void testTierStatusValues() {
        assertEquals(2, TierStatus.values().length);
        assertEquals(TierStatus.ACTIVE, TierStatus.valueOf("ACTIVE"));
        assertEquals(TierStatus.INACTIVE, TierStatus.valueOf("INACTIVE"));
    }

    @Test
    void testOrderStatusToString() {
        assertEquals("PENDING", OrderStatus.PENDING.toString());
        assertEquals("CONFIRMED", OrderStatus.CONFIRMED.toString());
        assertEquals("FAILED", OrderStatus.FAILED.toString());
    }

    @Test
    void testPaymentStatusToString() {
        assertEquals("PENDING", PaymentStatus.PENDING.toString());
        assertEquals("SUCCEEDED", PaymentStatus.SUCCEEDED.toString());
        assertEquals("FAILED", PaymentStatus.FAILED.toString());
    }

    @Test
    void testTierStatusToString() {
        assertEquals("ACTIVE", TierStatus.ACTIVE.toString());
        assertEquals("INACTIVE", TierStatus.INACTIVE.toString());
    }
}
