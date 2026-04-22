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
        String stripeEventId = "evt_123456";
        String eventType = "checkout.session.completed";
        String payload = "{\"id\":\"evt_123456\"}";

        PaymentEvent event = new PaymentEvent(paymentId, stripeEventId, eventType, payload);

        assertEquals(paymentId, event.getPaymentId());
        assertEquals(stripeEventId, event.getStripeEventId());
        assertEquals(eventType, event.getEventType());
        assertEquals(payload, event.getPayload());
    }

    @Test
    void testSettersAndGetters() {
        PaymentEvent event = new PaymentEvent();
        UUID id = UUID.randomUUID();
        UUID paymentId = UUID.randomUUID();
        String stripeEventId = "evt_789012";
        String eventType = "checkout.session.async_payment_failed";
        String payload = "{\"id\":\"evt_789012\",\"type\":\"payment.failed\"}";
        Instant processedAt = Instant.now();

        event.setId(id);
        event.setPaymentId(paymentId);
        event.setStripeEventId(stripeEventId);
        event.setEventType(eventType);
        event.setPayload(payload);
        event.setProcessedAt(processedAt);

        assertEquals(id, event.getId());
        assertEquals(paymentId, event.getPaymentId());
        assertEquals(stripeEventId, event.getStripeEventId());
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
