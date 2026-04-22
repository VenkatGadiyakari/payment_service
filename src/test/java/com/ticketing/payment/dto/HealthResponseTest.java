package com.ticketing.payment.dto;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class HealthResponseTest {

    @Test
    void testNoArgsConstructor() {
        HealthResponse response = new HealthResponse();
        assertNotNull(response);
    }

    @Test
    void testAllArgsConstructor() {
        String status = "UP";
        String service = "payment-service";
        String timestamp = "2026-04-21T10:00:00Z";

        HealthResponse response = new HealthResponse(status, service, timestamp);

        assertEquals(status, response.getStatus());
        assertEquals(service, response.getService());
        assertEquals(timestamp, response.getTimestamp());
    }

    @Test
    void testSettersAndGetters() {
        HealthResponse response = new HealthResponse();

        response.setStatus("DOWN");
        response.setService("test-service");
        response.setTimestamp("2026-04-21T11:00:00Z");

        assertEquals("DOWN", response.getStatus());
        assertEquals("test-service", response.getService());
        assertEquals("2026-04-21T11:00:00Z", response.getTimestamp());
    }
}
