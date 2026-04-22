package com.ticketing.payment.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class AuditServiceTest {

    private AuditService auditService;

    @BeforeEach
    void setUp() {
        auditService = new AuditService();
    }

    @Test
    void testLogOrderCreated() {
        assertDoesNotThrow(() ->
                auditService.logOrderCreated("order-123", "tier-456", 2, "buyer-789")
        );
    }

    @Test
    void testLogCheckoutSessionCreated() {
        assertDoesNotThrow(() ->
                auditService.logCheckoutSessionCreated("order-123", "cs_test_123456")
        );
    }

    @Test
    void testLogWebhookReceived() {
        assertDoesNotThrow(() ->
                auditService.logWebhookReceived("evt_123456", "checkout.session.completed")
        );
    }

    @Test
    void testLogPaymentSuccess() {
        assertDoesNotThrow(() ->
                auditService.logPaymentSuccess("order-123", "pi_123456", "250.50")
        );
    }

    @Test
    void testLogPaymentFailed() {
        assertDoesNotThrow(() ->
                auditService.logPaymentFailed("order-123", "Card declined")
        );
    }

    @Test
    void testLogInventoryDecremented() {
        assertDoesNotThrow(() ->
                auditService.logInventoryDecremented("tier-456", 3)
        );
    }

    @Test
    void testLogOversellDetected() {
        assertDoesNotThrow(() ->
                auditService.logOversellDetected("order-123", "tier-456")
        );
    }

    @Test
    void testLogOrderStatusUpdated() {
        assertDoesNotThrow(() ->
                auditService.logOrderStatusUpdated("order-123", "PENDING", "CONFIRMED")
        );
    }

    @Test
    void testLogDuplicateWebhook() {
        assertDoesNotThrow(() ->
                auditService.logDuplicateWebhook("evt_123456")
        );
    }

    @Test
    void testLogWebhookSignatureFailure() {
        assertDoesNotThrow(() ->
                auditService.logWebhookSignatureFailure("Invalid signature")
        );
    }

    @Test
    void testAllMethodsWithNullValues() {
        assertDoesNotThrow(() -> {
            auditService.logOrderCreated(null, null, null, null);
            auditService.logCheckoutSessionCreated(null, null);
            auditService.logWebhookReceived(null, null);
            auditService.logPaymentSuccess(null, null, null);
            auditService.logPaymentFailed(null, null);
            auditService.logInventoryDecremented(null, null);
            auditService.logOversellDetected(null, null);
            auditService.logOrderStatusUpdated(null, null, null);
            auditService.logDuplicateWebhook(null);
            auditService.logWebhookSignatureFailure(null);
        });
    }
}
