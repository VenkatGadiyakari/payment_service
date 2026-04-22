package com.ticketing.payment.controller;

import com.ticketing.payment.dto.HealthResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.*;

class HealthControllerTest {

    private HealthController healthController;

    @BeforeEach
    void setUp() {
        healthController = new HealthController();
    }

    @Test
    void testReadinessProbe() {
        ResponseEntity<HealthResponse> response = healthController.readinessProbe();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("UP", response.getBody().getStatus());
        assertEquals("payment-service", response.getBody().getService());
        assertNotNull(response.getBody().getTimestamp());
    }

    @Test
    void testLivenessProbe() {
        ResponseEntity<HealthResponse> response = healthController.livenessProbe();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("UP", response.getBody().getStatus());
        assertEquals("payment-service", response.getBody().getService());
        assertNotNull(response.getBody().getTimestamp());
    }

    @Test
    void testHealthEndpoint() {
        ResponseEntity<HealthResponse> response = healthController.health();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("UP", response.getBody().getStatus());
        assertEquals("payment-service", response.getBody().getService());
        assertNotNull(response.getBody().getTimestamp());
    }

    @Test
    void testAllEndpointsReturnConsistentStatus() {
        ResponseEntity<HealthResponse> readiness = healthController.readinessProbe();
        ResponseEntity<HealthResponse> liveness = healthController.livenessProbe();
        ResponseEntity<HealthResponse> health = healthController.health();

        assertEquals(readiness.getBody().getStatus(), liveness.getBody().getStatus());
        assertEquals(liveness.getBody().getStatus(), health.getBody().getStatus());
    }

    @Test
    void testAllEndpointsReturnConsistentServiceName() {
        ResponseEntity<HealthResponse> readiness = healthController.readinessProbe();
        ResponseEntity<HealthResponse> liveness = healthController.livenessProbe();
        ResponseEntity<HealthResponse> health = healthController.health();

        assertEquals(readiness.getBody().getService(), liveness.getBody().getService());
        assertEquals(liveness.getBody().getService(), health.getBody().getService());
    }
}
