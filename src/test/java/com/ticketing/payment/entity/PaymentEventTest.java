package com.ticketing.payment.entity;

import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class PaymentEventTest {

    @Test
    void testNoArgsConstructor() {
        PaymentEvent event = new PaymentEvent();
        assertNotNull(event);
    }

    @Test
    void testAllArgsConstructor() {
        UUID paymentId = UUID.randomUUID();
        String razorpayEventId = "pay_123456";
        String eventType = "payment_link.paid";
        String payload = "{\"event\":\"payment_link.paid\"}";

        PaymentEvent event = new PaymentEvent(paymentId, razorpayEventId, eventType, payload);

        assertEquals(paymentId, event.getPaymentId());
        assertEquals(razorpayEventId, event.getRazorpayEventId());
        assertEquals(eventType, event.getEventType());
        assertEquals(payload, event.getPayload());
    }

    @Test
    void testSettersAndGetters() {
        PaymentEvent event = new PaymentEvent();
        UUID id = UUID.randomUUID();
        UUID paymentId = UUID.randomUUID();
        String razorpayEventId = "pay_789012";
        String eventType = "payment.failed";
        String payload = "{\"event\":\"payment.failed\"}";
        Instant processedAt = Instant.now();

        event.setId(id);
        event.setPaymentId(paymentId);
        event.setRazorpayEventId(razorpayEventId);
        event.setEventType(eventType);
        event.setPayload(payload);
        event.setProcessedAt(processedAt);

        assertEquals(id, event.getId());
        assertEquals(paymentId, event.getPaymentId());
        assertEquals(razorpayEventId, event.getRazorpayEventId());
        assertEquals(eventType, event.getEventType());
        assertEquals(payload, event.getPayload());
        assertEquals(processedAt, event.getProcessedAt());
    }

    @Test
    void testProcessedAtField() {
        PaymentEvent event = new PaymentEvent();
        Instant now = Instant.now();

        event.setProcessedAt(now);

        assertNotNull(event.getProcessedAt());
        assertEquals(now, event.getProcessedAt());
    }
}
