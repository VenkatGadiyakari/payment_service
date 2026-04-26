package com.ticketing.payment.entity;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class PaymentTest {

    @Test
    void testNoArgsConstructor() {
        Payment payment = new Payment();
        assertNotNull(payment);
        assertEquals(PaymentStatus.PENDING, payment.getStatus());
        assertEquals("INR", payment.getCurrency());
    }

    @Test
    void testAllArgsConstructor() {
        UUID orderId = UUID.randomUUID();
        String razorpayPaymentId = "pay_123456";
        String razorpayOrderId = "order_123456";
        BigDecimal amount = BigDecimal.valueOf(100.50);
        String currency = "INR";

        Payment payment = new Payment(orderId, razorpayPaymentId, razorpayOrderId, amount, currency, PaymentStatus.SUCCEEDED);

        assertEquals(orderId, payment.getOrderId());
        assertEquals(razorpayPaymentId, payment.getRazorpayPaymentId());
        assertEquals(razorpayOrderId, payment.getRazorpayOrderId());
        assertEquals(amount, payment.getAmount());
        assertEquals(currency, payment.getCurrency());
        assertEquals(PaymentStatus.SUCCEEDED, payment.getStatus());
    }

    @Test
    void testSettersAndGetters() {
        Payment payment = new Payment();
        UUID id = UUID.randomUUID();
        UUID orderId = UUID.randomUUID();
        String razorpayPaymentId = "pay_789012";
        String razorpayOrderId = "order_789012";
        BigDecimal amount = BigDecimal.valueOf(250.75);

        payment.setId(id);
        payment.setOrderId(orderId);
        payment.setRazorpayPaymentId(razorpayPaymentId);
        payment.setRazorpayOrderId(razorpayOrderId);
        payment.setAmount(amount);
        payment.setCurrency("INR");
        payment.setStatus(PaymentStatus.FAILED);

        assertEquals(id, payment.getId());
        assertEquals(orderId, payment.getOrderId());
        assertEquals(razorpayPaymentId, payment.getRazorpayPaymentId());
        assertEquals(razorpayOrderId, payment.getRazorpayOrderId());
        assertEquals(amount, payment.getAmount());
        assertEquals("INR", payment.getCurrency());
        assertEquals(PaymentStatus.FAILED, payment.getStatus());
    }

    @Test
    void testDefaultCurrency() {
        Payment payment = new Payment();
        assertEquals("INR", payment.getCurrency());
    }

    @Test
    void testDefaultStatus() {
        Payment payment = new Payment();
        assertEquals(PaymentStatus.PENDING, payment.getStatus());
    }

    @Test
    void testTimestampFields() {
        Payment payment = new Payment();
        Instant before = Instant.now().minusSeconds(1);

        payment.setCreatedAt(Instant.now());
        payment.setUpdatedAt(Instant.now());

        Instant after = Instant.now().plusSeconds(1);

        assertNotNull(payment.getCreatedAt());
        assertNotNull(payment.getUpdatedAt());
        assertTrue(payment.getCreatedAt().isAfter(before));
        assertTrue(payment.getCreatedAt().isBefore(after));
    }
}
