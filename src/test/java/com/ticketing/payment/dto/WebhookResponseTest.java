package com.ticketing.payment.dto;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class WebhookResponseTest {

    @Test
    void testNoArgsConstructor() {
        WebhookResponse response = new WebhookResponse();
        assertNotNull(response);
    }

    @Test
    void testConstructorWithTrue() {
        WebhookResponse response = new WebhookResponse(true);
        assertTrue(response.isReceived());
    }

    @Test
    void testConstructorWithFalse() {
        WebhookResponse response = new WebhookResponse(false);
        assertFalse(response.isReceived());
    }

    @Test
    void testSetterAndGetter() {
        WebhookResponse response = new WebhookResponse();

        response.setReceived(true);
        assertTrue(response.isReceived());

        response.setReceived(false);
        assertFalse(response.isReceived());
    }
}
