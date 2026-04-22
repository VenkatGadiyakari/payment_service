package com.ticketing.payment.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class AuditService {

    private static final Logger logger = LoggerFactory.getLogger(AuditService.class);

    public void logOrderCreated(String orderId, String tierId, Integer quantity, String buyerId) {
        logger.info("AUDIT: Order created - orderId: {}, tierId: {}, quantity: {}, buyerId: ***",
                    orderId, tierId, quantity);
    }

    public void logCheckoutSessionCreated(String orderId, String sessionId) {
        logger.info("AUDIT: Stripe checkout session created - orderId: {}, sessionId: {}",
                    orderId, maskSensitiveData(sessionId));
    }

    public void logWebhookReceived(String eventId, String eventType) {
        logger.info("AUDIT: Webhook received - eventId: {}, eventType: {}", eventId, eventType);
    }

    public void logPaymentSuccess(String orderId, String paymentId, String amount) {
        logger.info("AUDIT: Payment succeeded - orderId: {}, paymentId: {}, amount: ***",
                    orderId, maskSensitiveData(paymentId));
    }

    public void logPaymentFailed(String orderId, String reason) {
        logger.warn("AUDIT: Payment failed - orderId: {}, reason: {}", orderId, reason);
    }

    public void logInventoryDecremented(String tierId, Integer quantity) {
        logger.info("AUDIT: Inventory decremented - tierId: {}, quantity: {}", tierId, quantity);
    }

    public void logOversellDetected(String orderId, String tierId) {
        logger.error("AUDIT: Oversell detected - orderId: {}, tierId: {}", orderId, tierId);
    }

    public void logOrderStatusUpdated(String orderId, String oldStatus, String newStatus) {
        logger.info("AUDIT: Order status updated - orderId: {}, oldStatus: {}, newStatus: {}",
                    orderId, oldStatus, newStatus);
    }

    public void logDuplicateWebhook(String eventId) {
        logger.info("AUDIT: Duplicate webhook detected - eventId: {}", eventId);
    }

    public void logWebhookSignatureFailure(String reason) {
        logger.error("AUDIT: Webhook signature verification failed - SECURITY ALERT - reason: {}", reason);
    }

    private String maskSensitiveData(String data) {
        if (data == null || data.length() <= 8) {
            return "***";
        }
        return data.substring(0, 4) + "***" + data.substring(data.length() - 4);
    }
}
