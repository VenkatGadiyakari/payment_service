package com.ticketing.payment.controller;

import com.ticketing.payment.dto.HealthResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.Instant;

@RestController
public class HealthController {

    @GetMapping("/health/ready")
    public ResponseEntity<HealthResponse> readinessProbe() {
        HealthResponse response = new HealthResponse(
                "UP",
                "payment-service",
                Instant.now().toString()
        );
        return ResponseEntity.ok(response);
    }

    @GetMapping("/health/live")
    public ResponseEntity<HealthResponse> livenessProbe() {
        HealthResponse response = new HealthResponse(
                "UP",
                "payment-service",
                Instant.now().toString()
        );
        return ResponseEntity.ok(response);
    }

    @GetMapping("/v1/payment/health")
    public ResponseEntity<HealthResponse> health() {
        HealthResponse response = new HealthResponse(
                "UP",
                "payment-service",
                Instant.now().toString()
        );
        return ResponseEntity.ok(response);
    }
}
